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

package org.hudsonci.maven.plugin.ui.gwt.configure;

import com.google.gwt.user.client.ui.HasWidgets;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.hudsonci.maven.plugin.ui.gwt.configure.workspace.WorkspaceManagerPresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Controls the MavenConfiguration module.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0.1
 */
@Singleton
public class MavenConfigurationController
{
    // FIXME: Not sure this is needed, keeping around for now until we have a better way to glue together modules for plugins and pages and such inside of Hudson

    private final WorkspaceManagerPresenter workspaceManagerPresenter;

    @Inject
    public MavenConfigurationController(final WorkspaceManagerPresenter workspaceManagerPresenter) {
        this.workspaceManagerPresenter = checkNotNull(workspaceManagerPresenter);
    }

    public void start(final HasWidgets container) {
        workspaceManagerPresenter.start(container);
    }
}