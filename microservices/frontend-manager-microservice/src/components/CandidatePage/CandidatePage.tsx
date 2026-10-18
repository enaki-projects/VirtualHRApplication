import React from "react";
import {Button, Col, InputGroup, Row} from "react-bootstrap";
import AvatarVideoPlayer from "../shared/Avatar/AvatarVideoPlayer";
import {AuthenticatedUser} from "../../models/authenticated-user";
import {authenticationService} from "../../services/AuthService";
import {avatarService} from "../../services/AvatarService";
import {environment} from "../../environments/environment";
import {candidateService} from "../../services/CandidateService";
import {CandidateCv} from "../../models/cv/candidate-cv";
import CandidateCard from "../shared/CandidateCard/CandidateCard";
import ReactLoading from "../../shared/Loading";

import {WithContext as ReactTags} from 'react-tag-input';
import "./CandidatePage.css"
import {myUtils} from "../../shared/utils";

const KeyCodes = {
    comma: 188,
    enter: [10, 13],
};
const delimiters = [...KeyCodes.enter, KeyCodes.comma];

export class CandidatePage extends React.Component<any, any> {
    currentUser: AuthenticatedUser
    candidates: CandidateCv[]
    private minAge: number = 0;
    private maxAge: number = 100;
    subscriptions = []
    _isMounted = false;

    constructor(props) {
        super(props);

        this.subscriptions.push(authenticationService.currentUser$.subscribe((user) => {
            this.currentUser = user;
        }));
        this.state = {
            loadingData: true,
            tags: [],
            minAge: 0,
            maxAge: 100,
            suggestions: [
                {id: 'Bitdefender', text: 'Bitdefender'},
                {id: 'Centric', text: 'Centric'},
                {id: 'C++', text: 'C++'},
                {id: 'Developer', text: 'Developer'},
                {id: 'Junior', text: 'Junior'},
                {id: 'Iasi', text: 'Iasi'},
                {id: 'Continental', text: 'Continental'},
            ]
        };
        this.candidates = [];

        this.handleDelete = this.handleDelete.bind(this);
        this.handleAddition = this.handleAddition.bind(this);
        this.handleDrag = this.handleDrag.bind(this);

        this.handleMaxAge = this.handleMaxAge.bind(this);
        this.handleMinAge = this.handleMinAge.bind(this);

        this.resetFilters = this.resetFilters.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
    }

    resetFilters() {
        if (!this._isMounted) return;
        this.setState({
            tags: [],
            minAge: 0,
            maxAge: 100,
        });
        this.setState({loadingData: true});
        candidateService.fetchCandidatesCv().then(response => {
            this.candidates = response.data;
        }).finally(() => {
            if (!this._isMounted) return;
            this.setState({loadingData: false});
        });
    }

    handleSearch() {
        if (!this._isMounted) return;
        this.setState({loadingData: true});
        const criteria = {
            "min_age": this.state.minAge,
            "max_age": this.state.maxAge,
            "keywords": myUtils.extractFieldFromListOfDict(this.state.tags, "text"),
        }
        candidateService.fetchCandidatesCvByCriteria(criteria).then(response => {
            this.candidates = response.data;
        }).finally(() => {
            if (!this._isMounted) return;
            this.setState({loadingData: false});
        });
    }

    async componentDidMount() {
        this._isMounted = true;
        this.candidates = (await candidateService.fetchCandidatesCv()).data;

        avatarService.updateAvatarVideo(environment.avatarScenarios.candidates);
        this.setState({loadingData: false});
    }

    handleDelete(i) {
        if (!this._isMounted) return;

        const {tags} = this.state;
        this.setState({
            tags: tags.filter((tag, index) => index !== i),
        });
    }

    handleAddition(tag) {
        if (!this._isMounted) return;

        this.setState(state => ({tags: [...state.tags, tag]}));
    }

    handleDrag(tag, currPos, newPos) {
        if (!this._isMounted) return;

        const tags = [...this.state.tags];
        const newTags = tags.slice();

        newTags.splice(currPos, 1);
        newTags.splice(newPos, 0, tag);

        // re-render
        this.setState({tags: newTags});
    }


    handleMinAge(event) {
        if (!this._isMounted) return;

        this.setState({
            minAge: event.target.value || 0
        });
    }

    handleMaxAge(event) {
        if (!this._isMounted) return;

        this.setState({
            maxAge: event.target.value || 0
        });
    }

    componentWillUnmount() {
        this.subscriptions.forEach(subscription => {
            subscription.unsubscribe();
        })
        this.subscriptions = [];
    }

    render() {
        const {tags, suggestions} = this.state;

        return <div className="d-flex">
            <Row className="mt-4 w-100 justify-content-between">

                <Col xl={8} lg={8} md={8} sm={12} xs={12} className="text-center">
                    <Row className="ms-2 me-2 w-100 border rounded bg-custom border-light">
                        <Col className="mt-2" xl={8} lg={8} md={8} sm={12} xs={12}>
                            <Row>
                                <InputGroup className="col-6 col-sm-6 col-md-6 col-lg-6 col-xl-6">
                                    <div className="input-group-text input-group-text-cst ">Min Age</div>
                                    <input type="number" value={this.state.minAge} className="form-control"
                                           onChange={this.handleMinAge}/>
                                </InputGroup>
                                <InputGroup className="col-6 col-sm-6 col-md-6 col-lg-6 col-xl-6">
                                    <div className="input-group-text input-group-text-cst ">Max Age</div>
                                    <input type="number" value={this.state.maxAge} className="form-control"
                                           onChange={this.handleMaxAge}/>
                                </InputGroup>
                            </Row>
                            <Row className="mt-3 mb-2">
                                <InputGroup className="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                                    <InputGroup className="input-group ">
                                    <span
                                        className="input-group-text input-group-text-area col-12 input-group-text-cst text-light">Keywords: </span>
                                        <ReactTags tags={tags}
                                                   suggestions={suggestions}
                                                   handleDelete={this.handleDelete}
                                                   handleAddition={this.handleAddition}
                                                   handleDrag={this.handleDrag}
                                                   delimiters={delimiters}
                                                   placeholder="Add a keyword"
                                        />
                                    </InputGroup>
                                </InputGroup>
                            </Row>
                        </Col>
                        <Col xl={4} lg={4} md={4} sm={12} xs={12}>

                            <Button className="btn btn-success w-100 m-2" onClick={this.handleSearch}>
                                Apply Filters
                            </Button>
                            <Button className="btn btn-info w-100 m-2" onClick={this.resetFilters}>
                                Reset Filters
                            </Button>
                        </Col>
                    </Row>
                    {
                        !this.state.loadingData ?
                            <Row className="ms-2 me-2 mt-3">
                                {
                                    this.candidates ? this.candidates.map(item =>
                                        <Col className="mb-4" key={item.uid} xl={3} lg={3} md={4} sm={12} xs={12}>
                                            <CandidateCard user={item}/>
                                        </Col>) : <h2>No Candidates Matching this criteria Found</h2>}
                            </Row> :
                                <Row className="text-center justify-content-center align-items-center">
                                    <ReactLoading type="bars" color="purple" height={667} width={375}/>
                                </Row>
                    }
                </Col>
                <Col xl={4} lg={4} md={4} sm={12} xs={12}>
                    <AvatarVideoPlayer/>
                </Col>
            </Row>
        </div>;
    }
}
