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

import static org.fuin.owndeb.commons.DebUtils.writeReplacedResource;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.objects4j.common.NotEmpty;
import org.fuin.objects4j.common.Nullable;
import org.fuin.owndeb.commons.DebDependency;
import org.fuin.owndeb.commons.DebPackage;
import org.fuin.owndeb.commons.DebPackages;
import org.fuin.owndeb.commons.DebUtils;
import org.fuin.owndeb.pkg.base.AbstractDownloadTarGzPackage;

/**
 * Downloads and Oracle JDK and creates a binary Debian package from it.
 */
@XmlRootElement(name = "jdk-package")
public final class JdkPackage extends AbstractDownloadTarGzPackage {

    /** Name of the package. */
    public static final String NAME = "jdk-package";

    /**
     * Default constructor for JAXB.
     */
    protected JdkPackage() {
        super();
    }

    /**
     * Constructor with dependency array.
     * 
     * @param name
     *            Unique package name.
     * @param version
     *            Package version.
     * @param description
     *            Package description.
     * @param maintainer
     *            Maintainer of the package.
     * @param arch
     *            Architecture identifier like "amd64".
     * @param installationPath
     *            Installation path like "/opt".
     * @param section
     *            Section like "devel".
     * @param priority
     *            Priority like "low".
     * @param jdkUrl
     *            URL with "tar.gz" JDK file.
     * @param dependencies
     *            Array of dependencies.
     */
    public JdkPackage(@NotEmpty final String name,
            @Nullable final String version, @Nullable final String description,
            @Nullable final String maintainer, @Nullable final String arch,
            @Nullable final String installationPath,
            @Nullable final String section, @Nullable final String priority,
            @NotNull final String jdkUrl,
            @Nullable final DebDependency... dependencies) {
        super(name, version, description, maintainer, arch, installationPath,
                section, priority, jdkUrl, dependencies);
    }

    /**
     * Constructor with dependency list.
     * 
     * @param name
     *            Unique package name.
     * @param version
     *            Package version.
     * @param description
     *            Package description.
     * @param maintainer
     *            Maintainer of the package.
     * @param arch
     *            Architecture identifier like "amd64".
     * @param installationPath
     *            Installation path like "/opt".
     * @param section
     *            Section like "devel".
     * @param priority
     *            Priority like "low".
     * @param jdkUrl
     *            URL with "tar.gz" JDK file.
     * @param dependencies
     *            List of dependencies.
     */
    public JdkPackage(@NotEmpty final String name,
            @Nullable final String version, @Nullable final String description,
            @Nullable final String maintainer, @Nullable final String arch,
            @Nullable final String installationPath,
            @Nullable final String section, @Nullable final String priority,
            @NotNull final String jdkUrl,
            @Nullable final List<DebDependency> dependencies) {
        super(name, version, description, maintainer, arch, installationPath,
                section, priority, jdkUrl, dependencies);
    }

    @Override
    public final String getPackageName() {
        return NAME;
    }

    @Override
    public final void init(@Nullable final DebPackages parent) {
        addNonExistingVariables(parent);
        initDownloadTarGzPackage(parent);
        resolveVariables();
    }

    @Override
    protected final void applyModifications(final File packageDir) {
        // No modifications
    }

    @Override
    protected final void copyControlFiles(final File controlDir) {
        final Map<String, String> vars = DebUtils.asMap(getParent()
                .getVariables());
        writeReplacedResource(JdkPackage.class, "/" + getPackageName()
                + "/control", controlDir, vars);
        writeReplacedResource(JdkPackage.class, "/" + getPackageName()
                + "/postinst", controlDir, vars);
    }

    @Override
    public final String toString() {
        return getPackageName();
    }

}
