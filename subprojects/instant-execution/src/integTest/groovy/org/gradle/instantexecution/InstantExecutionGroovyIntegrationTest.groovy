/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.instantexecution


import spock.lang.Ignore

class InstantExecutionGroovyIntegrationTest extends AbstractInstantExecutionIntegrationTest {

    @Ignore
    def "instant execution for compileGroovy on Groovy project with no dependencies"() {
        given:
        buildFile << """
            plugins { id 'groovy' }
            
            println "running build script"
        """
        file("src/main/java/Thing.groovy") << """
            class Thing {
            }
        """

        expect:
        instantRun "compileGroovy"
        outputContains("running build script")
        result.assertTasksExecuted(":compileGroovy")
        def classFile = file("build/classes/java/main/Thing.class")
        classFile.isFile()

        when:
        classFile.delete()
        instantRun "compileGroovy"

        then:
        outputDoesNotContain("running build script")
        result.assertTasksExecuted(":compileGroovy")
        classFile.isFile()
    }
}