/**
 * The MIT License
 *
 * Copyright (c) 2010-2011 Sonatype, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.hudsonci.maven.eventspy.common;

import org.hudsonci.maven.model.state.ArtifactDTO;
import org.hudsonci.maven.model.state.MavenProjectDTO;
import org.hudsonci.maven.model.state.RuntimeEnvironmentDTO;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Provides the interface for invoking call backs from remote EventSpy to MavenBuilder.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0.1
 */
public interface Callback
{
    /**
     * A reference to the <tt>.maven</tt> directory for the executing build.
     */
    File getMavenContextDirectory(); // FIXME: This does not belong here, more of a impl-side context detail

    /**
     * Returns true if the build should be aborted.
     */
    boolean isAborted();
    
    /**
     * Called to signal the end of processing.
     */
    void close();

    /**
     * Called after the spy has finished initializing and has established communication.
     */
    void setRuntimeEnvironment(RuntimeEnvironmentDTO env);

    /**
     * Prefer them to be sorted in topological order because we don't order them.
     */
    void setParticipatingProjects( List<MavenProjectDTO> projects );
    
    void updateParticipatingProject( MavenProjectDTO project );

    void setArtifacts(Collection<ArtifactDTO> artifacts);

    void addArtifacts(Collection<ArtifactDTO> artifacts);

    DocumentReference getSettingsDocument();

    DocumentReference getGlobalSettingsDocument();

    DocumentReference getToolChainsDocument();
}