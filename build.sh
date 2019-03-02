CLASSPATH=build-common/target/build-common-0.0.1-SNAPSHOT.jar:buildsystem-common/target/buildsystem-common-0.0.1-SNAPSHOT.jar:buildsystem-maven/target/buildsystem-maven-0.0.1-SNAPSHOT.jar:util/target/util-0.0.1-SNAPSHOT.jar:structured-log-model/target/structured-log-model-0.0.1-SNAPSHOT.jar:bytecode-common/target/bytecode-common-0.0.1-SNAPSHOT.jar:build-main/target/build-main-0.0.1-SNAPSHOT.jar:component-java-language/target/component-java-language-0.0.1-SNAPSHOT.jar:~/projects/compiler/compiler-common/target/compiler-common-0.0.1-SNAPSHOT.jar:~/projects/compiler/bytecode-common/target/bytecode-common-0.0.1-SNAPSHOT.jar:~/projects/compiler/bytecode-java/target/bytecode-java-0.0.1-SNAPSHOT.jar

args=$@

java -cp $CLASSPATH com.neaterbits.ide.buildmain.BuildMain $args


