import {Component, Input, OnInit} from '@angular/core';
import {UserSkill} from '../../_models/cv/references/user-skill';
import {SkillService} from '../../_services/cv-references/skill.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-cv-editor-skills',
  templateUrl: './cv-editor-skills.component.html',
  styleUrls: ['./cv-editor-skills.component.css']
})
export class CvEditorSkillsComponent implements OnInit {
  @Input() interpersonalSkills: UserSkill[] = [];
  @Input() digitalSkills: UserSkill[] = [];
  @Input() leadershipSkills: UserSkill[] = [];
  @Input() organisationalSkills: UserSkill[] = [];

  globalInterpersonalSkills: UserSkill[] = [];
  globalDigitalSkills: UserSkill[] = [];
  globalLeadershipSkills: UserSkill[] = [];
  globalOrganisationalSkills: UserSkill[] = [];
  items = [{name: 'items1'}, {name: 'item21'}];
  constructor(
    private skillService: SkillService,
    private toastr: ToastrService,
  ) {
    this.skillService.getSkills('interpersonal').then((userSkills) => {
      this.globalInterpersonalSkills = userSkills;
    });
    this.skillService.getSkills('digital').then((userSkills) => {
      this.globalDigitalSkills = userSkills;
    });
    this.skillService.getSkills('leadership').then((userSkills) => {
      this.globalLeadershipSkills = userSkills;
    });
    this.skillService.getSkills('organisational').then((userSkills) => {
      this.globalOrganisationalSkills = userSkills;
    });
  }

  ngOnInit(): void {
  }

  onSkillRemoved(skill: any, skillType: string): void {
    if (skill){
      this.skillService.deleteSkill(skill, skillType).toPromise().then(() => {
        this.skillService.fetchDeletedUserSkill(skill, skillType);
        this.toastr.info(`${this.skillService.toTitleCase(skillType)} Skill Removed`);
      }, error => {
        console.log(error);
        this.toastr.error(error.error.status, error.error.error);
      });
    }
  }

  onSkillAdded(skill: any, skillType: string): void {
    // console.log(this[`${skillType}Skills`]);
    if (skill){
      this[`${skillType}Skills`].pop();

      this.skillService.addSkill(skill, skillType).toPromise().then(() => {
        this.skillService.fetchUserSkill(skill, skillType).toPromise().then((userSkill: UserSkill) => {
          this.toastr.success(`${this.skillService.toTitleCase(skillType)} Skill Added`);
          this[`${skillType}Skills`].push(userSkill);
        });
      }, error => {
        console.log(error);
        if (error.error.status === 400){
          this.toastr.error('You have already the skill ' + skill.name);
        } else {
          this.toastr.error(error.error.status, error.error.error);
        }
      });
    }
  }
}
