/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.owndeb.pkg.jdk;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.fuin.owndeb.commons.DebPackage;
import org.fuin.owndeb.commons.DebPackageResolver;
import org.fuin.owndeb.pkg.jdk.JdkPackage;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the {@link JdkPackage} class.
 */
// CHECKSTYLE:OFF for tests
public final class JdkPackageTest {

    @Ignore("Download is too big - Think about better solution later")
    @Test
    public final void testCreate() {

        // PREPARE
        final String name = "jdk8";
        final String version = "1.8.0.60";
        final String description = "Java SE Development Kit 8";
        final String maintainer = "michael@fuin.org";
        final String arch = "amd64";
        final String installationPath = "/opt";
        final String section = "devel";
        final String priority = "low";
        final String jdkUrl = "http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-x64.tar.gz";
        final JdkPackage testee = new JdkPackage(name, version, description,
                maintainer, arch, installationPath, section, priority, jdkUrl);
        testee.init(null);
        testee.resolveDependencies(new DebPackageResolver() {
            @Override
            public DebPackage findDebPackage(String packageName) {
                // Nothing to resolve
                return null;
            }
        });

        final File buildDir = new File("./target");

        // TEST
        testee.create(buildDir);

        // VERIFY
        final File changesFile = new File(buildDir,
                "jdk8_1.8.0.60_amd64.changes");
        final File debFile = new File(buildDir, "jdk8_1.8.0.60_amd64.deb");
        assertThat(changesFile).exists();
        assertThat(debFile).exists();

    }

}
// CHECKSTYLE:ON
