package kind2.util;

import static java.util.stream.Collectors.toList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.w3c.dom.Element;

import kind2.Kind2Exception;
import kind2.lustre.EnumType;
import kind2.lustre.NamedType;
import kind2.lustre.SubrangeIntType;
import kind2.lustre.Type;
import kind2.lustre.VarDecl;
import kind2.lustre.values.ArrayValue;
import kind2.lustre.values.BooleanValue;
import kind2.lustre.values.EnumValue;
import kind2.lustre.values.IntegerValue;
import kind2.lustre.values.RealValue;
import kind2.lustre.values.Value;

public class Util {

    public static <T> List<T> safeList(Collection<? extends T> original) {
        if (original == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(new ArrayList<>(original));
        }
    }

    public static <T> List<T> safeNullableList(List<? extends T> original) {
        if (original == null) {
            return null;
        } else {
            return Collections.unmodifiableList(new ArrayList<>(original));
        }
    }

    public static <T> SortedMap<String, T> safeStringSortedMap(Map<String, T> original) {
        TreeMap<String, T> map = new TreeMap<>(new StringNaturalOrdering());
        if (original != null) {
            map.putAll(original);
        }
        return Collections.unmodifiableSortedMap(map);
    }

    public static <T> List<T> copyNullable(List<? extends T> original) {
        if (original == null) {
            return null;
        }
        return new ArrayList<>(original);
    }

    public static void writeToFile(String content, File file) throws IOException {
        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content);
        }
    }

    public static Set<String> safeStringSortedSet(Collection<String> original) {
        TreeSet<String> set = new TreeSet<>(new StringNaturalOrdering());
        set.addAll(original);
        return Collections.unmodifiableSet(set);
    }

    public static Set<List<String>> safeStringSortedSets(Set<List<String>> original) {
        Set<List<String>> set = new HashSet<>(new TreeSet<>(new StringNaturalOrdering()));
        for (Collection<String> currentSetOriginal : original) {
            TreeSet<String> individualSet = new TreeSet<>(new StringNaturalOrdering());
            individualSet.addAll(currentSetOriginal);
            set.add(safeList(individualSet));
        }

        return Collections.unmodifiableSet(set);
    }

    public static List<String> getIds(List<VarDecl> decls) {
        return decls.stream().map(decl -> decl.id).collect(toList());
    }

    public static Value promoteIfNeeded(Value value, Type type) {
        if (value instanceof IntegerValue && type == NamedType.REAL) {
            IntegerValue iv = (IntegerValue) value;
            return new RealValue(new BigFraction(iv.value));
        }
        return value;
    }

    public static String removeTrailingZeros(String str) {
        if (!str.contains(".")) {
            return str;
        }

        return str.replaceFirst("\\.?0*$", "");
    }

    public static Value parseValue(String type, String value) {
        switch (type) {
            case "bool":
                if (value.equals("0") || value.equals("false") || value.equals("False")) {
                    return BooleanValue.FALSE;
                } else if (value.equals("1") || value.equals("true") || value.equals("True")) {
                    return BooleanValue.TRUE;
                }
                break;

            case "int":
                return new IntegerValue(new BigInteger(value));

            case "real":

                // Sally returns real values with decimal points
                // Question: how to manage precision loss?
                if (value.contains(".")) {
                    double x = Double.parseDouble(value.toString());
                    return new RealValue(BigFraction.valueOf(BigDecimal.valueOf(x)));
                }

                String[] strs = value.split("/");
                if (strs.length <= 2) {
                    BigInteger num = new BigInteger(strs[0]);
                    BigInteger denom = strs.length > 1 ? new BigInteger(strs[1]) : BigInteger.ONE;
                    return new RealValue(new BigFraction(num, denom));
                }
                break;

            default:
                if (value.isEmpty() || Character.isAlphabetic(value.charAt(0))) {
                    return new EnumValue(value);
                } else {
                    throw new IllegalArgumentException("Invalid enumeration value: " + value);
                }
        }

        throw new Kind2Exception("Unable to parse " + value + " as " + type);
    }

    public static Value parseArrayValue(String type, Element arrayElement) {
        int size = Integer.parseInt(arrayElement.getAttribute("size"));
        List<Value> elements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Value elValue;
            Element arrayEl = getElement(arrayElement, "Array", i);
            if (arrayEl != null) {
                elValue = parseArrayValue(type, arrayEl);
            } else {
                arrayEl = getElement(arrayElement, "Item", i);
                int index = Integer.parseInt(arrayEl.getAttribute("index"));
                if (index != i) {
                    throw new IllegalArgumentException("We expect array indicies to be sorted");
                }
                elValue = parseValue(type, arrayEl.getTextContent());
            }
            elements.add(elValue);
        }
        return new ArrayValue(elements);
    }

    private static Element getElement(Element element, String name, int index) {
        return (Element) element.getElementsByTagName(name).item(index);
    }

    public static Value parseValue(Type type, String value) {
        return parseValue(getName(type), value);
    }

    /*
     * Get the name of the type as modeled by the SMT solvers
     */
    public static String getName(Type type) {
        if (type instanceof NamedType) {
            NamedType namedType = (NamedType) type;
            return namedType.name;
        } else if (type instanceof SubrangeIntType || type instanceof EnumType) {
            return "int";
        } else {
            throw new IllegalArgumentException("Cannot find name for type " + type);
        }
    }

    /**
     * In SMT solvers, integer division behaves differently than in Java. In
     * particular, for -5 div 3 java says '-1' and SMT solvers say '-2'
     */
    public static BigInteger smtDivide(BigInteger a, BigInteger b) {
        return a.subtract(a.mod(b)).divide(b);
    }

    /** Default name for realizability query property in XML file */
    public static final String REALIZABLE = "%REALIZABLE";

    /**
     * ASCII "End of Text" character, used by Kind2Api to ask Kind2 to terminate
     */
    public static final int END_OF_TEXT = 0x03;
}
