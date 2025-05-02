import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Project} from '../../_models/cv/references/project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  currentUser: AuthenticatedUser;
  baseUrl = environment.adapterApiUrl;

  constructor(
    private accountService: AccountService,
    private http: HttpClient
  ) {
    accountService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
  }

  addProject(projectModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/projects`, projectModel).pipe(
      map((projectId: any) => {
        // console.log('[addProject] - ' + projectId.Id);
        return projectId.Id;
      })
    );
  }

  fetchUserProject(projectId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/projects/${projectId}`).pipe(
      map((project: Project) => {
        // console.log('[fetchUserProject] - ' + project);
        this.currentUser.cv.cvReferences.projects = this.filteredProject(projectId);
        this.currentUser.cv.cvReferences.projects.push(project);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedProject(projectId): void{
    this.currentUser.cv.cvReferences.projects = this.filteredProject(projectId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredProject(projectId: number): Project[]{
    return this.currentUser.cv.cvReferences.projects.filter(item => item.projectId !== projectId);
  }

  updateProject(projectId: number, projectModel: any): Observable<any> {
    projectModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/projects/${projectId}`, projectModel).pipe(
      map((result: any) => {
        // console.log('[updateProject] - ' + result);
      })
    );
  }

  deleteProject(projectId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/projects/${projectId}`).pipe(
      map((result: any) => {
        // console.log('[deleteProject] - ' + result);
      })
    );
  }
}
