import 'express-session';

declare module 'express-session' {
    interface SessionData {
        // Shape comes from cv-adapter's login response plus the user's CV.
        currentUser?: any;
    }
}
