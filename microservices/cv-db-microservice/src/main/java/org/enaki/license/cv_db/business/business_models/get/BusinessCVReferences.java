package org.enaki.license.cv_db.business.business_models.get;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessCVReferences {
    public String serverAddress;

    public BusinessCVReferences(String serverAddress) {
        this.serverAddress = serverAddress;
        iqTests = new LinkedList<>();
    }

    public List<String> locations;
    public List<String> citizenship;
    public List<String> awards;
    public List<String> hobbies;
    public List<String> projects;
    public List<String> phoneNumbers;
    public List<String> publications;
    public List<String> iqTests;
    public List<String> userLanguages;
    public List<String> educationalExperiences;
    public List<String> professionalExperiences;
    public List<String> userInterpersonalSkills;
    public List<String> userDigitalSkills;
    public List<String> userOrganisationalSkills;
    public List<String> userLeadershipSkills;

    public void setCitizenship(List<Long> citizenship) {
        this.citizenship = citizenship
                .stream()
                .map(it -> String.format("%s/api/geography/nationalities/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setAwards(List<Long> awards) {
        this.awards = awards
                .stream()
                .map(it -> String.format("%s/api/cv/awards/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setHobbies(List<Long> hobbies) {
        this.hobbies = hobbies
                .stream()
                .map(it -> String.format("%s/api/cv/hobbies/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setProjects(List<Long> projects) {
        this.projects = projects
                .stream()
                .map(it -> String.format("%s/api/cv/projects/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setPhoneNumbers(List<Long> phoneNumbers) {
        this.phoneNumbers = phoneNumbers
                .stream()
                .map(it -> String.format("%s/api/cv/phone-numbers/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setPublications(List<Long> publications) {
        this.publications = publications
                .stream()
                .map(it -> String.format("%s/api/cv/publications/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setIqTests(List<Long> iqTests) {
        this.iqTests = iqTests
                .stream()
                .map(it -> String.format("%s/api/cv/iq-tests/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setUserLanguages(List<Long> userLanguages) {
        this.userLanguages = userLanguages
                .stream()
                .map(it -> String.format("%s/api/cv/user-languages/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setEducationalExperiences(List<Long> educationalExperiences) {
        this.educationalExperiences = educationalExperiences
                .stream()
                .map(it -> String.format("%s/api/cv/educational-experiences/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setProfessionalExperiences(List<Long> professionalExperiences) {
        this.professionalExperiences = professionalExperiences
                .stream()
                .map(it -> String.format("%s/api/cv/professional-experiences/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setUserInterpersonalSkills(List<Long> userInterpersonalSkills) {
        this.userInterpersonalSkills = userInterpersonalSkills
                .stream()
                .map(it -> String.format("%s/api/cv/skills/interpersonal/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setUserDigitalSkills(List<Long> userDigitalSkills) {
        this.userDigitalSkills = userDigitalSkills
                .stream()
                .map(it -> String.format("%s/api/cv/skills/digital/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setUserOrganisationalSkills(List<Long> userOrganisationalSkills) {
        this.userOrganisationalSkills = userOrganisationalSkills
                .stream()
                .map(it -> String.format("%s/api/cv/skills/organisational/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setUserLeadershipSkills(List<Long> userLeadershipSkills) {
        this.userLeadershipSkills = userLeadershipSkills
                .stream()
                .map(it -> String.format("%s/api/cv/skills/leadership/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

    public void setLocations(List<Long> locationsIds){
        this.locations = locationsIds
                .stream()
                .map(it -> String.format("%s/api/cv/locations/%s", serverAddress, it))
                .collect(Collectors.toList());
    }

}
