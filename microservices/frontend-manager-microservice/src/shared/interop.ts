// Some CommonJS packages (react-loading, react-rating) reach Vite's bundle as
// { default: Component } instead of Component. Unwrap once, at import time.
export function cjsDefault<T>(mod: T): T {
    const m = mod as unknown as { default?: T };
    return (m && typeof m === 'object' && m.default) ? m.default : mod;
}
