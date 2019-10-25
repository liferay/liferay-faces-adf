/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */
package com.liferay.faces.adf.base.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.share.security.credentialstore.Credential;
import oracle.adf.share.security.identitymanagement.AttributeFilter;
import oracle.adf.share.security.identitymanagement.Role;
import oracle.adf.share.security.identitymanagement.User;
import oracle.adf.share.security.identitymanagement.spi.IdentityManagement;


/**
 * Provides a minimial implementation of an ADF identity provider that can be specified in an adf-config.xml descriptor.
 * For example:
 *
 * <pre>
&lt;adf-config xmlns="http://xmlns.oracle.com/adf/config"
               xmlns:sec="http://xmlns.oracle.com/adf/security/config"&gt;
     &lt;sec:AdfSecurityConfigType&gt;
         &lt;sec:JaasSecurityContextType&gt;
             &lt;sec:contextEnv name="oracle.adf.security.identity.provider" value="com.liferay.faces.adf.security.IdentityProvider" /&gt;
         &lt;/sec:JaasSecurityContextType&gt;
     &lt;/sec:AdfSecurityConfigType&gt;
 &lt;/adf-config&gt;
 </pre>
 *
 * @author  Neil Griffin
 */
public class IdentityProvider implements IdentityManagement {

	@Override
	public Principal addRole(Role roleDef) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addToRole(Principal roleRef, Principal member) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal addUser(User user, Credential credential) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Role createRole() {
		throw new UnsupportedOperationException();
	}

	@Override
	public User createUser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteFromRole(Principal roleRef, Principal member) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteRole(Principal principal) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteUser(Principal principal) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getAnonymousRole() {
		return null;
	}

	@Override
	public String getAnonymousRoleName() {
		return null;
	}

	@Override
	public Principal getAnonymousUser() {
		return null;
	}

	@Override
	public String getAnonymousUserName() {
		return null;
	}

	@Override
	public Role getRole(Principal principal) {
		return null;
	}

	@Override
	public ArrayList getRoleList(int sizeLimit, AttributeFilter[] attributeFilters) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getRolePrincipal(String roleName) {
		return null;
	}

	@Override
	public User getUser(Principal principal) {
		return null;
	}

	@Override
	public ArrayList getUserList(int sizeLimit, AttributeFilter[] attributeFilters) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ArrayList getUserList(int sizeLimit, AttributeFilter[] attributeFilters, Principal principal) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getUserPrincipal(String username) {
		return null;
	}

	@Override
	public ArrayList getUserProfileList(int sizeLimit, AttributeFilter[] attributeFilters) {
		return null;
	}

	@Override
	public Object getUserProfilePropertyVal(String name, String propName) {

		if ("GUID".equals(propName)) {
			return name;
		}
		else if ("USER_NAME".equals(propName)) {
			return name;
		}

		return null;
	}

	@Override
	public boolean isAddRoleSupported() {
		return false;
	}

	@Override
	public boolean isAddUserSupported() {
		return false;
	}

	@Override
	public boolean isDeleteRoleSupported() {
		return false;
	}

	@Override
	public boolean isDeleteUserSupported() {
		return false;
	}

	@Override
	public boolean isModifyRoleSupported() {
		return false;
	}

	@Override
	public boolean isModifyUserSupported() {
		return false;
	}

	@Override
	public void modifyRole(Principal roleRef, Role roleDef) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void modifyUser(Principal principal, User user) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void saveUserProfile(String userName, HashMap values) {
		throw new UnsupportedOperationException();
	}
}
