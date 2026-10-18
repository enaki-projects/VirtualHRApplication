export const environment = {
    production: false,
    userFrontendUrl: 'http://localhost:3000/',
    adapterApiUrl: 'http://localhost:5000/api/',
    avatarApiUrl: 'http://localhost:2010/api/',
    avatarScenarios: {
        home: {
            name: 'manager_intro_1',
            subcategory: 'intro',
            category: 'manager'
        },
        candidates: {
            name: 'manager_candidates_1',
            subcategory: 'candidates',
            category: 'manager'
        },
        carts: {
            name: 'manager_carts_1',
            subcategory: 'carts',
            category: 'manager'
        },
    }
};

/* For Production
export const environment = {
    production: false,
    userFrontendUrl: 'http://localhost:3000/',
    adapterApiUrl: 'https://virtual-hr-aggregator.herokuapp.com/api/',
    avatarApiUrl: 'https://murynyo.go.ro:2010/api/',
    avatarScenarios: {
        home: {
            name: 'manager_intro_1',
            subcategory: 'intro',
            category: 'manager'
        },
        candidates: {
            name: 'manager_candidates_1',
            subcategory: 'candidates',
            category: 'manager'
        },
        carts: {
            name: 'manager_carts_1',
            subcategory: 'carts',
            category: 'manager'
        },
    }
};
*/

