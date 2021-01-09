package com.neaterbits.ide.component.runners.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;

import javax.xml.bind.JAXB;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.build.types.ModuleId;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.language.Language;
import com.neaterbits.build.types.resource.ModuleResource;
import com.neaterbits.build.types.resource.NamespaceResource;
import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResource;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResource;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;

public class RunnerConfigurationTest {

    @Test
    public void testSerialization() {
        
        final String [] namespace = new String [] { "com", "test", "namespace" };
        
        final SourceFileResourcePath sourceFileResourcePath
            = new SourceFileResourcePath(
                    new NamespaceResourcePath(
                            new SourceFolderResourcePath(
                                    new ProjectModuleResourcePath(
                                            Arrays.asList(
                                                    new ModuleResource(
                                                            new ModuleId("root"),
                                                            new File("rootdir")),
                                                    new ModuleResource(
                                                            new ModuleId("module"),
                                                            new File("moduledir")))
                                            ),
                                    new SourceFolderResource(
                                            new File("src/main/java"),
                                            "src",
                                            Language.JAVA)),
                            new NamespaceResource(
                                    new File("com/test/namespace"),
                                    namespace)),
                    new SourceFileResource(new File("SourceFile.java")));
        
        final RunConfiguration runConfiguration = new RunConfiguration();
        
        runConfiguration.setName("RunConfiguration name");
        
        runConfiguration.setSourceFile(sourceFileResourcePath);
        
        final String [] outerTypes = new String [] { "OuterType" };
        
        runConfiguration.setMainClass(new TypeName(
                namespace,
                outerTypes,
                "Type"));
        runConfiguration.setProgramArguments("the program arguments");
    
        runConfiguration.setVmArguments("the vm arguments");
    
        final RunnersConfiguration runnersConfiguration = new RunnersConfiguration();
        
        runnersConfiguration.setRunConfigurations(Arrays.asList(runConfiguration));
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        JAXB.marshal(runnersConfiguration, baos);
        
        final byte [] bytes = baos.toByteArray();
        
        System.out.println("## xml " + new String(bytes));
    
        final RunnersConfiguration unmarshalled
            = JAXB.unmarshal(
                    new ByteArrayInputStream(bytes),
                    RunnersConfiguration.class);

        assertThat(unmarshalled.getRunConfigurations().size()).isEqualTo(1);
        
        final RunConfiguration c = unmarshalled.getRunConfigurations().get(0);
        
        assertThat(c.getName()).isEqualTo("RunConfiguration name");
        assertThat(c.getMainClass().getNamespace()).isEqualTo(namespace);
        assertThat(c.getMainClass().getOuterTypes()).isEqualTo(outerTypes);
        assertThat(c.getMainClass().getName()).isEqualTo("Type");
        
        assertThat(c.getProgramArguments()).isEqualTo("the program arguments");
        assertThat(c.getVmArguments()).isEqualTo("the vm arguments");
        
        assertThat(c.getSourceFile()).isNotNull();
        assertThat(c.getSourceFile().getFile().getPath()).isEqualTo("SourceFile.java");
        
        assertThat(c.getSourceFile().getNamespacePath().getNamespaceResource().getNamespace())
            .isEqualTo(namespace);
        
        assertThat(c.getSourceFile().getSourceFolderPath().getSourceFolder().getLanguage())
            .isEqualTo(Language.JAVA);
        assertThat(c.getSourceFile().getSourceFolderPath().getSourceFolder().getName())
            .isEqualTo("src");
        assertThat(c.getSourceFile().getModule().length()).isEqualTo(2);
        assertThat(c.getSourceFile().getModule().getModuleId().getId()).isEqualTo("module");
        assertThat(c.getSourceFile().getModule().getRoot().getModuleId().getId()).isEqualTo("root");
    }
}
