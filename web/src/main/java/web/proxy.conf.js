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
      "/register"
    ],
    target: "http://localhost:8080",
    secure: false
  }
]

module.exports = PROXY_CONFIG;
