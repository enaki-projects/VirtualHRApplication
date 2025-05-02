import "./CandidateCvView.scss"
import React, {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {candidateService} from "../../../services/CandidateService";
import ReactLoading from "react-loading";
import {Col, Container, Row} from "react-bootstrap";
import {authenticationService} from "../../../services/AuthService";
import {userService} from "../../../services/UserService";
import {
    faArrowLeft,
    faBriefcase,
    faBuilding,
    faHome,
    faPhoneAlt,
    faUser,
    IconDefinition
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {CandidateCv} from "../../../models/cv/candidate-cv";
import {Hobby} from "../../../models/cv/references/hobby";
import {UserSkill} from "../../../models/cv/references/user-skill";
import {EducationalExperience} from "../../../models/cv/references/educational-experience";
import {myUtils} from "../../../shared/utils";
import {ProfessionalExperience} from "../../../models/cv/references/professional-experience";
import {Project} from "../../../models/cv/references/project";
import {Award} from "../../../models/cv/references/award";
import {Publication} from "../../../models/cv/references/publication";
import {PhoneNumber} from "../../../models/cv/references/phone-number";
import {UserLocation} from "../../../models/cv/references/userLocation";


function getLocationIcon(locationType: string): IconDefinition {
    switch (locationType.toLowerCase()) {
        case "home":
            return faHome;
        case "work":
            return faBriefcase;
        default:
            return faBuilding;
    }
}

export default function CandidateCvView() {
    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [candidateCv, setCandidateCv] = useState(null);

    let {id, cartId}: any = useParams()
    useEffect(() => {
        candidateService.fetchCandidatesCvById(id)
            .then(
                (response) => {
                    setIsLoaded(true);
                    setCandidateCv(response.data);
                },
                (error) => {
                    setIsLoaded(true);
                    setError(error);
                }
            )
    }, [id])
    if (error) {
        return <div>Error: {error.message}</div>;
    } else if (!isLoaded || candidateCv == null) {
        return <Row className="text-center justify-content-center align-items-center">
            <ReactLoading type="bars" color="purple" height={667} width={375}/>
        </Row>
    } else {
        const currentCv: CandidateCv = candidateCv;
        const photoUrl = authenticationService.getUserPhotoUrl(currentCv.hasImage, currentCv.uid);
        const age = userService.getAge(currentCv.birthdate);
        return (
            <Container className="justify-content-center">
                <Row>

                    <Col lg={{span: 10, offset: 1}}
                         xl={{span: 10, offset: 1}}
                         md={{span: 10, offset: 1}}>

                        <section className="panel panel-default p-5 bg-light mb-3">
                            <div className="panel-body">
                                <Link to={(cartId)? `/cart/${cartId}` : "/candidates"} className="btn btn-primary position-absolute"><FontAwesomeIcon icon={faArrowLeft}/> Back</Link>
                                <article className="panel-body">
                                    <figure className="text-center">
                                        <img src={photoUrl}
                                             className="img-thumbnail rounded-circle img-responsive" alt="me"/>
                                        <figcaption>
                                            <h3>{currentCv.firstname} {currentCv.lastname}</h3>
                                            <div className="container-fluid col-8 text-left">
                                                <FontAwesomeIcon icon={faUser}/>
                                                <strong> {currentCv.email}</strong>
                                                {renderLocations(currentCv.cvReferences.locations)}
                                                {renderPhoneNumbers(currentCv.cvReferences.phoneNumbers)}
                                            </div>
                                        </figcaption>
                                    </figure>
                                </article>
                                <br/>
                                <article>
                                    <h4 className="cv-section-title">
                                        <strong>Personal Details</strong>
                                    </h4>
                                    <hr/>
                                    <dl className="row">
                                        <dt className="col-4">Date of Birth:</dt>
                                        <dd className="col-7">{currentCv.birthdate}</dd>
                                        <dt className="col-4">Age:</dt>
                                        <dd className="col-7">{age}</dd>
                                        <dt className="col-4">About:</dt>
                                        {(currentCv.aboutMe) ? <dd className="col-7">{currentCv.aboutMe}</dd> :
                                            <dd className="col-7">-</dd>}
                                        <dt className="col-4">Motto:</dt>
                                        {(currentCv.aboutMe) ? <dd className="col-7">{currentCv.motto}</dd> :
                                            <dd className="col-7">-</dd>}
                                    </dl>
                                </article>
                                {renderHobbies(currentCv.cvReferences.hobbies)}
                                {renderSkills(currentCv.cvReferences.userInterpersonalSkills,
                                    currentCv.cvReferences.userLeadershipSkills,
                                    currentCv.cvReferences.userOrganisationalSkills,
                                    currentCv.cvReferences.userDigitalSkills
                                )}
                                {renderEducationalExperiences(currentCv.cvReferences.educationalExperiences)}
                                {renderProfessionalExperiences(currentCv.cvReferences.professionalExperiences)}
                                {renderProjects(currentCv.cvReferences.projects)}
                                {renderAwards(currentCv.cvReferences.awards)}
                                {renderPublications(currentCv.cvReferences.publications)}

                                <article>
                                    <div className="d-flex flex-row-reverse">
                                        <a className='ml-1 mr-1' href="https://github.com/enaki" title="Enaki's Github">
                                            <img className='icon-class' src="/assets/hr_logo_blue.png" alt="vhr_logo"/>
                                        </a>
                                        <a className='ml-1 mr-1' href="https://github.com/enaki" title="Enaki's Github">
                                            <img className='icon-class' src="/assets/hr_logo_gray.png" alt="vhr_logo"/>
                                        </a>
                                        <a className='ml-1 mr-1' href="https://github.com/enaki" title="Enaki's Github">
                                            <img className='icon-class' src="/assets/hr_logo_green.png" alt="vhr_logo"/>
                                        </a>
                                        <a className='ml-1 mr-1' href="https://github.com/enaki" title="Enaki's Github">
                                            <img className='icon-class' src="/assets/hr_logo_red.png" alt="vhr_logo"/>
                                        </a>
                                    </div>
                                </article>
                            </div>
                        </section>
                    </Col>
                </Row>
            </Container>
        );
    }
}


function renderHobbies(hobbies: Hobby[]) {
    const hobbiesUnited: string = hobbies.map(hobby => myUtils.toTitleCase(hobby.name)).join(", ");
    if (hobbies.length > 0) {
        return (
            <article>
                <h4 className="cv-section-title">
                    <strong>Hobbies</strong>
                </h4>
                <hr/>
                {hobbiesUnited}
            </article>
        )
    }
    return null;
}

function renderSkills(interpersonalSkills: UserSkill[],
                      leadershipSkills: UserSkill[],
                      organisationalSkills: UserSkill[],
                      technicalSkills: UserSkill[]) {

    if (interpersonalSkills.length > 0 || leadershipSkills.length > 0 || organisationalSkills.length > 0 || technicalSkills.length > 0) {
        const interpersonalUnited: string = interpersonalSkills.map(skill => skill.name).join(", ");
        let interpersonalSection = (interpersonalSkills.length > 0) ?
            <dl className="row">
                <dt className="col-3">Interpersonal Skill:</dt>
                <dd className="col-7"> {interpersonalUnited}</dd>
            </dl> : null
        const leadershipUnited: string = leadershipSkills.map(skill => skill.name).join(", ");
        let leadershipSection = (leadershipSkills.length > 0) ?
            <dl className="row">
                <dt className="col-3">Leadership Skill:</dt>
                <dd className="col-7"> {leadershipUnited}</dd>
            </dl> : null
        const organisationalUnited: string = organisationalSkills.map(skill => skill.name).join(", ");
        let organisationalSection = (organisationalSkills.length > 0) ?
            <dl className="row">
                <dt className="col-3">Organisational Skill:</dt>
                <dd className="col-7"> {organisationalUnited}</dd>
            </dl> : null
        const technicalUnited: string = technicalSkills.map(skill => skill.name).join(", ");
        let technicalSection = (technicalSkills.length > 0) ?
            <dl className="row">
                <dt className="col-3">Technical Skill:</dt>
                <dd className="col-7"> {technicalUnited}</dd>
            </dl> : null

        return (
            <article>
                <h4 className="cv-section-title">
                    <strong>Hobbies</strong>
                </h4>
                <hr/>
                {interpersonalSection}
                {leadershipSection}
                {organisationalSection}
                {technicalSection}
            </article>
        )
    }
    return null;
}


function renderEducationalExperiences(educationalExperiences: EducationalExperience[]) {
    if (educationalExperiences.length > 0) {
        return <article>
            <h4 className="cv-section-title">
                <strong>Education</strong>
            </h4>
            <div>
                {educationalExperiences.map(educationalExperience =>
                    <div key={"eduExp" + educationalExperience.educationalExperienceId}>
                        <hr/>
                        <dl className="dl-horizontal">
                            <dt>From <span className="cv-dates">{educationalExperience.fromDate}</span> to <span
                                className="cv-dates">{educationalExperience.toDate || 'Present'}</span> <span
                                className="float-right">{educationalExperience.qualificationTitle}</span></dt>
                            <div className="offset-4 col-8">
                                <dd className="text-right"><span
                                    className="title-style">"{educationalExperience.educationalInstitution.institutionName}"
                            </span>, {educationalExperience.educationalInstitution.country.countryName}
                                    &nbsp;<span
                                        className={"flag-icon flag-icon-" + educationalExperience.educationalInstitution.country.iso2.toLowerCase()}/>
                                </dd>
                            </div>
                        </dl>
                    </div>
                )}
            </div>
        </article>
    }
    return null;
}

function renderProfessionalExperiences(professionalExperiences: ProfessionalExperience[]) {
    if (professionalExperiences.length > 0) {
        return <article>
            <h4 className="cv-section-title">
                <strong>Work Experiences</strong>
            </h4>
            <div>
                {professionalExperiences.map(professionalExperience =>
                    <div key={"proExp" + professionalExperience.professionalExperienceId}>
                        <hr/>
                        <dl className="dl-horizontal">
                            <dt>From <span className="cv-dates">{professionalExperience.fromDate}</span> to <span
                                className="cv-dates">{professionalExperience.toDate || 'Present'}</span> <span
                                className="float-right">{myUtils.toTitleCase(professionalExperience.jobName)}</span>
                            </dt>
                            <div className="offset-4 col-8">
                                <dd className="text-right"><span className="title-style">{
                                    professionalExperience.organisation.organisationName
                                }</span>, {professionalExperience.organisation.city.cityName},
                                    {professionalExperience.organisation.city.region.country.countryName}
                                    &nbsp;<span
                                        className={"flag-icon flag-icon-" + professionalExperience.organisation.city.region.country.iso2.toLowerCase()}/>
                                </dd>
                            </div>
                        </dl>
                    </div>)}
            </div>
        </article>
    }
    return null;
}

function renderProjects(projects: Project[]) {
    if (projects.length > 0) {
        return <article>
            <h4 className="cv-section-title">
                <strong>Projects</strong>
            </h4>
            <div>
                {projects.map(project =>
                    <div key={"project" + project.projectId}>
                        <hr/>
                        <dl className="dl-horizontal">
                            <dt>From <span className="cv-dates">{project.fromDate}</span> to <span
                                className="cv-dates">{project.toDate || 'Present'}</span></dt>
                            <div className="offset-4 col-8">
                                <dd><strong>Name:</strong> {project.name}</dd>

                                {(project.description) ?
                                    <dd><strong>Description:</strong> {project.description}</dd> : null}
                                {(project.url) ? <dd><strong><a href={project.url}>See Project</a></strong></dd> : null}
                            </div>
                        </dl>
                    </div>)}
            </div>
        </article>
    }
    return null;
}

function renderAwards(awards: Award[]) {
    if (awards.length > 0) {
        return <article>
            <h4 className="cv-section-title">
                <strong>Awards</strong>
            </h4>
            <div>
                {awards.map(award =>
                    <div key={"award" + award.awardId}>
                        <hr/>
                        <dl className="dl-horizontal">
                            <dt>From <span className="cv-dates">{award.dateOfAward}</span></dt>
                            <div className="offset-4 col-8">
                                <dd><strong>Name:</strong> {award.title}</dd>
                                <dd><strong>Awarding Institution:</strong> {award.awardingInstitution}</dd>
                                {(award.description) ?
                                    <dd><strong>Description:</strong> {award.description}</dd> : null}
                            </div>
                        </dl>
                    </div>)}
            </div>
        </article>
    }
    return null;
}

function renderPublications(publications: Publication[]) {
    if (publications.length > 0) {
        return <article>
            <h4 className="cv-section-title">
                <strong>Publications</strong>
            </h4>
            <div>
                {publications.map(publication =>
                    <div key={"publication" + publication.publicationId}>
                        <hr/>
                        <dl className="dl-horizontal">
                            <dt>From <span className="cv-dates">{publication.year}</span></dt>
                            <div className="offset-4 col-8">
                                <dd><strong>Name:</strong> {publication.title}</dd>
                                {(publication.reference) ?
                                    <dd><strong>References:</strong> {publication.reference}</dd> : null}
                                {(publication.description) ?
                                    <dd><strong>Description:</strong> {publication.description}</dd> : null}
                                {(publication.url) ?
                                    <dd><a href={publication.url}>See Publication</a></dd> : null}
                            </div>
                        </dl>
                    </div>)}
            </div>
        </article>
    }
    return null;
}

function renderLocations(locations: UserLocation[]) {
    if (locations.length > 0) {
        return <div className="mt-2">
            {locations.map(location =>
                <div key={'location' + location.locationId}>
                    <FontAwesomeIcon icon={getLocationIcon(location.type)}/>
                    <strong>&nbsp;{location.type}</strong>:&nbsp;
                    <span
                        className={'flag-icon flag-icon-' + location.city.region.country.iso2.toLowerCase()}/> &nbsp;
                    {location.city.region.country.countryName},
                    {location.city.cityName},
                    {location.address}
                </div>
            )}
        </div>
    }
    return null;
}

function renderPhoneNumbers(phoneNumbers: PhoneNumber[]) {
    if (phoneNumbers.length > 0) {
        return <div className="mt-2">
            {phoneNumbers.map(phoneNumber =>
                <div key={'phoneNumber' + phoneNumber.phoneNumberId}>
                    <FontAwesomeIcon
                        icon={faPhoneAlt}/> +<strong>{phoneNumber.country.phoneCode}</strong> {phoneNumber.number}
                </div>
            )}
        </div>
    }
    return null;
}
