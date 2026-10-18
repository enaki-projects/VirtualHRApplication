// Production environment. Served as a static SPA behind the gateway nginx at base href '/'.
// API calls are same-origin relative paths; the gateway proxies them to the backend services.
export const environment = {
  production: true,
  adapterApiUrl: '/api/',
  avatarApiUrl: '/avatar-api/',
  avatarScenarios: {
    home: {
      name: 'candidate_intro_1',
      subcategory: 'intro',
      category: 'candidate'
    },
    cv_page: {
      name: 'cv_page_1',
      subcategory: 'page',
      category: 'cv'
    },
    cv_view_page: {
      name: 'cv_view_page_1',
      subcategory: 'view_page',
      category: 'cv'
    },
    cv_personal_info: {
      name: 'cv_personal_info_1',
      subcategory: 'personal_info',
      category: 'cv'
    },
    cv_contact: {
      name: 'cv_contact_1',
      subcategory: 'contact',
      category: 'cv'
    },
    cv_nationality: {
      name: 'cv_nationality_1',
      subcategory: 'nationality',
      category: 'cv'
    },
    cv_language: {
      name: 'cv_language_1',
      subcategory: 'language',
      category: 'cv'
    },
    cv_location: {
      name: 'cv_location_1',
      subcategory: 'location',
      category: 'cv'
    },
    cv_hobby: {
      name: 'cv_hobby_1',
      subcategory: 'hobby',
      category: 'cv'
    },
    cv_projects: {
      name: 'cv_projects_1',
      subcategory: 'projects',
      category: 'cv'
    },
    cv_publications: {
      name: 'cv_publications_1',
      subcategory: 'publications',
      category: 'cv'
    },
    cv_awards: {
      name: 'cv_awards_1',
      subcategory: 'awards',
      category: 'cv'
    },
    cv_educational_experiences: {
      name: 'cv_educational_experiences_1',
      subcategory: 'educational_experiences',
      category: 'cv'
    },
    cv_professional_experiences: {
      name: 'cv_professional_experiences_1',
      subcategory: 'professional_experiences',
      category: 'cv'
    },
    cv_skills: {
      name: 'cv_skills_1',
      subcategory: 'skills',
      category: 'cv'
    },
    iq_test: {
      name: 'iq_intro_1',
      subcategory: 'intro',
      category: 'iq'
    },
    iq_test_result: {
      name: 'iq_result_1',
      subcategory: 'result',
      category: 'iq'
    },
    iq_test_analysis: {
      name: 'iq_analysis_1',
      subcategory: 'analysis',
      category: 'iq'
    }
  }
};
