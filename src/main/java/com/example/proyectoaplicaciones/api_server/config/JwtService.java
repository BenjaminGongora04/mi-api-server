    // ANTES: public boolean isTokenValid(String token, User user)
    public boolean isTokenValid(String token, String userEmail) { // AHORA: Recibe solo el email
        final String username = extractUsername(token);
        return (username.equals(userEmail)) && !isTokenExpired(token);
    }
