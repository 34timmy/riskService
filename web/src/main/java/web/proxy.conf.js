const PROXY_CONFIG = [
  {
    context: [
      "/getData",
      "/constructor",
      "/companies",
      "/perform",
      "/import",
      "/login",
      "/auth",
      "/refresh",
      "/register",
      "/profile"
      // "/copy"
    ],
    target: "http://localhost:8080",
    secure: false
  }
]

module.exports = PROXY_CONFIG;
