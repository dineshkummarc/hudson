/*******************************************************************************
 *
 * Copyright (c) 2009, Yahoo!, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *
 *   
 *      
 *
 *******************************************************************************/ 

package hudson.tasks.junit;

import hudson.Launcher;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.Builder;
import hudson.tasks.test.TestResult;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.recipes.LocalData;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.*;

/**
 * Test the mechanism for calling a JUnitParser separately
 * from the JUnitResultsArchiver
 *
 */
public class JUnitParserTest extends HudsonTestCase {
    static hudson.tasks.junit.TestResult theResult = null;

    public static final class JUnitParserTestBuilder extends Builder implements Serializable {
        private String testResultLocation;
        public JUnitParserTestBuilder(String testResultLocation) {
            this.testResultLocation = testResultLocation;
        }

        @Override
        public boolean perform(AbstractBuild<?, ?> build,
                               Launcher launcher, BuildListener listener)
                throws InterruptedException, IOException {
            System.out.println("in perform...");

            // First touch all the files, so they will be recently modified
            for (FilePath f : build.getWorkspace().list()) {
                f.touch(System.currentTimeMillis());
            }

            System.out.println("...touched everything");
            hudson.tasks.junit.TestResult result = (new JUnitParser()).parse( testResultLocation, build, launcher, listener);

            System.out.println("back from parse");
            assertNotNull("we should have a non-null result", result);
            assertTrue("result should be a TestResult", result instanceof hudson.tasks.junit.TestResult);
            System.out.println("We passed some assertions in the JUnitParserTestBuilder");
            theResult = result;
            return (result != null);
        }
    }


    private FreeStyleProject project;
    private String projectName =  "junit_parser_test";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        theResult = null; 
        project = createFreeStyleProject(projectName);
        project.getBuildersList().add(new JUnitParserTestBuilder("*.xml"));
    }

    @LocalData
    public void testJustParsing() throws Exception {
        FreeStyleBuild build = project.scheduleBuild2(0).get(100, TimeUnit.MINUTES);
        assertNotNull(build);
        
        // Now let's examine the result. We know lots of stuff about it because
        // we've analyzed the xml source files by hand.
        assertNotNull("we should have a result in the static member", theResult);

        // Check the overall counts. We should have 1 failure, 0 skips, and 132 passes.
        Collection<? extends TestResult> children = theResult.getChildren();
        assertFalse("Should have several packages", children.isEmpty());
        assertTrue("Should have several pacakges", children.size() > 3); 
        int passCount = theResult.getPassCount();
        assertEquals("expecting many passes", 131, passCount);
        int failCount = theResult.getFailCount();
        assertEquals("we should have one failure", 1, failCount);
        assertEquals("expected 0 skips", 0, theResult.getSkipCount());
        assertEquals("expected 132 total tests", 132, theResult.getTotalCount()); 

        // Dig in to the failed test 
        final String EXPECTED_FAILING_TEST_NAME = "testDataCompatibilityWith1_282";
        final String EXPECTED_FAILING_TEST_CLASSNAME = "hudson.security.HudsonPrivateSecurityRealmTest";

        Collection<? extends TestResult> failingTests = theResult.getFailedTests();
        assertEquals("should have one failed test", 1, failingTests.size());
        Map<String, TestResult> failedTestsByName = new HashMap<String, hudson.tasks.test.TestResult>();
        for (TestResult r: failingTests) {
            failedTestsByName.put(r.getName(), r);  
        }
        assertTrue("we've got the expected failed test", failedTestsByName.containsKey(EXPECTED_FAILING_TEST_NAME));
        TestResult firstFailedTest = failedTestsByName.get(EXPECTED_FAILING_TEST_NAME);
        assertFalse("should not have passed this test", firstFailedTest.isPassed());

        // TODO: Dig in to the passed tests, too



    }


}
