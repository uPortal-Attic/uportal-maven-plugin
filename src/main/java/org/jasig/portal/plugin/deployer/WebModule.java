/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portal.plugin.deployer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents a //application/module/web Node in the EAR descriptor.
 * 
 * @author Eric Dalquist
 * @version $Revision$
 */
public class WebModule {
    private String webUri;
    private String contextRoot;
    
    
    public String getContextRoot() {
        return this.contextRoot;
    }
    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }
    public String getWebUri() {
        return this.webUri;
    }
    public void setWebUri(String webUri) {
        this.webUri = webUri;
    }
    
    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof WebModule)) {
            return false;
        }
        WebModule rhs = (WebModule)object;
        return new EqualsBuilder()
            .append(this.contextRoot, rhs.contextRoot)
            .append(this.webUri, rhs.webUri)
            .isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-110713495, -1544877739)
            .append(this.contextRoot)
            .append(this.webUri)
            .toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("contextRoot", this.contextRoot)
            .append("webUri", this.webUri)
            .toString();
    }
}
