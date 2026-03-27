const TOKEN_KEY = "token"
/**
 * get Token
 */
export function getToken() {
    return sessionStorage.getItem(TOKEN_KEY);
}
/**
 * set Token
 */
export function setToken(token) {
    sessionStorage.setItem(TOKEN_KEY, token);
}
/**
 * remove Token
 */
export function clearToken() {
    sessionStorage.setItem(TOKEN_KEY, null);
}