package be.ehb.model.requests;

import be.ehb.model.backup.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestoreBackupRequest implements Serializable {

    private LocalDate dateOfBackup;
    private Set<OrganizationBackup> organizations;
    private Set<ProjectBackup> projects;
    private Set<ActivityBackup> activities;
    private Set<PaygradeBackup> paygrades;
    private Set<UserBackup> users;
    private Set<ProjecAssignmentBackup> assignments;
    private Set<RoleBackup> roles;
    private Set<MembershipBackup> memberships;
    private Set<MailTemplateBackup> mailTemplates;
    private Set<ConfigurationBackup> configurations;
    private Set<WorklogBackup> worklogs;

    public LocalDate getDateOfBackup() {
        return dateOfBackup;
    }

    public void setDateOfBackup(LocalDate dateOfBackup) {
        this.dateOfBackup = dateOfBackup;
    }

    public Set<OrganizationBackup> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<OrganizationBackup> organizations) {
        this.organizations = organizations;
    }

    public Set<ProjectBackup> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectBackup> projects) {
        this.projects = projects;
    }

    public Set<ActivityBackup> getActivities() {
        return activities;
    }

    public void setActivities(Set<ActivityBackup> activities) {
        this.activities = activities;
    }

    public Set<PaygradeBackup> getPaygrades() {
        return paygrades;
    }

    public void setPaygrades(Set<PaygradeBackup> paygrades) {
        this.paygrades = paygrades;
    }

    public Set<UserBackup> getUsers() {
        return users;
    }

    public void setUsers(Set<UserBackup> users) {
        this.users = users;
    }

    public Set<ProjecAssignmentBackup> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<ProjecAssignmentBackup> assignments) {
        this.assignments = assignments;
    }

    public Set<RoleBackup> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleBackup> roles) {
        this.roles = roles;
    }

    public Set<MembershipBackup> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<MembershipBackup> memberships) {
        this.memberships = memberships;
    }

    public Set<MailTemplateBackup> getMailTemplates() {
        return mailTemplates;
    }

    public void setMailTemplates(Set<MailTemplateBackup> mailTemplates) {
        this.mailTemplates = mailTemplates;
    }

    public Set<ConfigurationBackup> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Set<ConfigurationBackup> configurations) {
        this.configurations = configurations;
    }

    public Set<WorklogBackup> getWorklogs() {
        return worklogs;
    }

    public void setWorklogs(Set<WorklogBackup> worklogs) {
        this.worklogs = worklogs;
    }

    @Override
    public String toString() {
        return "RestoreBackupRequest{" +
                "organizations=" + organizations +
                ", projects=" + projects +
                ", activities=" + activities +
                ", paygrades=" + paygrades +
                ", users=" + users +
                ", assignments=" + assignments +
                ", roles=" + roles +
                ", memberships=" + memberships +
                ", mailTemplates=" + mailTemplates +
                ", configurations=" + configurations +
                ", worklogs=" + worklogs +
                '}';
    }
}