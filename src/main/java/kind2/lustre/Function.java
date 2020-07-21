package kind2.lustre;

import java.util.Collections;
import java.util.List;

import kind2.Assert;
import kind2.util.Util;

public class Function extends Ast {
	public final String id;
	public final List<VarDecl> inputs;
	public final List<VarDecl> outputs;

	public Function(Location location, String id, List<VarDecl> inputs, List<VarDecl> outputs) {
		super(location);
		Assert.isNotNull(id);
		this.id = id;
		this.inputs = Util.safeList(inputs);
		this.outputs = Util.safeList(outputs);
	}

	public Function(String id, List<VarDecl> inputs, List<VarDecl> outputs) {
		this(Location.NULL, id, inputs, outputs);
	}

	public Function(String id, List<VarDecl> inputs, VarDecl output) {
		this(Location.NULL, id, inputs, Collections.singletonList(output));
	}
}
