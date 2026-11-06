/*
 * Copyright (c) 2026, Board of Trustees of the University of Iowa
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License. See LICENSE in the project root for license information.
 */

package edu.uiowa.cs.clc.kind2.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class SafeModeTest {
  private static void assertHasOption(List<String> command, String option, String value) {
    int index = command.indexOf(option);
    assertTrue(index >= 0, option + " is missing from " + command);
    assertEquals(value, command.get(index + 1));
  }

  @Test
  void dockerRunCommandRestrictsContainer() throws IOException {
    Path kind2 = Files.createTempFile("kind2", null);
    String originalKind2 = Kind2Api.KIND2;
    try {
      Kind2Api.KIND2 = kind2.toAbsolutePath().toString();
      List<String> command = new Kind2Api().getDockerRunCommand("name");

      assertHasOption(command, "--network", "none");
      assertHasOption(command, "--pids-limit", "1024");
      assertHasOption(command, "--cap-drop", "ALL");
      assertHasOption(command, "--security-opt", "no-new-privileges");
      assertHasOption(command, "--user", "65534:65534");
      assertHasOption(command, "--tmpfs", "/tmp");
      assertTrue(command.contains("--read-only"));
    } finally {
      Kind2Api.KIND2 = originalKind2;
      Files.delete(kind2);
    }
  }

  @Test
  void interpreterFileIsReadableOnlyThroughAMount() throws IOException {
    assumeTrue(FileSystems.getDefault().supportedFileAttributeViews().contains("posix"));

    File file = ApiUtil.writeInterpreterFile("[]");
    try {
      assertEquals(Collections.singletonList("[]"), Files.readAllLines(file.toPath()));
      assertTrue(Files.getPosixFilePermissions(file.toPath())
          .contains(PosixFilePermission.OTHERS_READ));
      assertEquals("rwx------", PosixFilePermissions
          .toString(Files.getPosixFilePermissions(file.getParentFile().toPath())));
    } finally {
      Files.delete(file.toPath());
      Files.delete(file.getParentFile().toPath());
    }
  }
}
