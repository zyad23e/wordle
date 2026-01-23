import { useState } from "react";

const API_BASE = import.meta.env.VITE_API_URL ?? "http://localhost:8080";


export default function Login({ onLoginSuccess }) {
  const [mode, setMode] = useState("login"); // "login" | "register"
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    if (loading) return;

    setLoading(true);
    setError("");

    const endpoint =
      mode === "login" ? "/api/auth/login" : "/api/auth/register";

    try {
      const res = await fetch(`${API_BASE}${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          username: username.trim(),
          password,
        }),
      });

      if (!res.ok) {
        if (mode === "register" && res.status === 409) {
          setError("Username already taken");
          return;
        }
        setError(
          mode === "login"
            ? "Invalid username or password"
            : "Registration failed"
        );
        return;
      }

      if (mode === "register") {
        const loginRes = await fetch(`${API_BASE}/api/auth/login`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({
            username: username.trim(),
            password,
          }),
        });

        if (!loginRes.ok) {
          setError("Registered, but login failed. Try logging in.");
          return;
        }
      }

      onLoginSuccess();
    } catch {
      setError("Network error");
    } finally {
      setLoading(false);
    }
  }

  const page = {
    minHeight: "100vh",
    display: "grid",
    placeItems: "center",
    padding: 24,
    background:
      "radial-gradient(1200px 800px at 20% 10%, #1a1a1a, #0b0b0b 60%, #050505)",
    color: "white",
    fontFamily: "system-ui",
  };

  const card = {
    width: "100%",
    maxWidth: 440,
    borderRadius: 18,
    border: "1px solid rgba(255,255,255,0.12)",
    background: "rgba(20,20,20,0.75)",
    backdropFilter: "blur(10px)",
    padding: 20,
    boxShadow: "0 12px 40px rgba(0,0,0,0.55)",
  };

  const titleRow = {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    marginBottom: 18,
  };

  const title = {
    margin: 0,
    fontSize: 26,
    fontWeight: 900,
    letterSpacing: 0.3,
  };

  const pills = {
    display: "flex",
    gap: 8,
  };

  const pill = (active) => ({
    padding: "8px 12px",
    borderRadius: 12,
    border: "1px solid rgba(255,255,255,0.18)",
    background: active
      ? "rgba(255,255,255,0.18)"
      : "rgba(0,0,0,0.35)",
    color: "white",
    cursor: loading ? "not-allowed" : "pointer",
    fontWeight: 800,
  });

  const formColumn = {
    maxWidth: 340,
    margin: "0 auto",
  };

  const label = {
    fontSize: 12,
    opacity: 0.75,
    marginBottom: 6,
  };

  const input = {
    width: "100%",
    padding: "12px 12px",
    borderRadius: 12,
    border: "1px solid rgba(255,255,255,0.14)",
    background: "rgba(0,0,0,0.35)",
    color: "white",
    outline: "none",
    fontSize: 14,
  };

  const primaryBtn = {
    width: "100%",
    marginTop: 14,
    padding: "12px 12px",
    borderRadius: 12,
    border: "1px solid rgba(255,255,255,0.2)",
    background: "rgba(255,255,255,0.18)",
    color: "white",
    cursor: loading ? "not-allowed" : "pointer",
    fontWeight: 900,
    letterSpacing: 0.2,
  };

  const helper = {
    marginTop: 10,
    opacity: 0.7,
    fontSize: 12,
    textAlign: "center",
  };

  const errorBox = {
    marginTop: 12,
    padding: "10px 12px",
    borderRadius: 12,
    background: "rgba(255, 80, 80, 0.12)",
    border: "1px solid rgba(255, 80, 80, 0.25)",
    color: "#ffb3b3",
    fontWeight: 800,
    fontSize: 13,
  };

  return (
    <div style={page}>
      <div style={card}>
        <div style={titleRow}>
          <h2 style={title}>Wordle Web</h2>
          <div style={pills}>
            <button
              type="button"
              onClick={() => {
                setMode("login");
                setError("");
              }}
              style={pill(mode === "login")}
              disabled={loading}
            >
              Login
            </button>
            <button
              type="button"
              onClick={() => {
                setMode("register");
                setError("");
              }}
              style={pill(mode === "register")}
              disabled={loading}
            >
              Register
            </button>
          </div>
        </div>

        <form onSubmit={handleSubmit}>
          <div style={formColumn}>
            <div style={{ marginBottom: 14 }}>
              <div style={label}>Username</div>
              <input
                style={input}
                placeholder="e.g. JohnDoe"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                autoComplete="username"
                disabled={loading}
              />
            </div>

            <div style={{ marginBottom: 6 }}>
              <div style={label}>Password</div>
              <input
                style={input}
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                autoComplete={
                  mode === "login" ? "current-password" : "new-password"
                }
                disabled={loading}
              />
            </div>

            <button type="submit" style={primaryBtn} disabled={loading}>
              {loading
                ? mode === "login"
                  ? "Logging in…"
                  : "Creating account…"
                : mode === "login"
                ? "Login"
                : "Create account"}
            </button>

            {error && <div style={errorBox}>{error}</div>}

            <div style={helper}>
              {mode === "register"
                ? "Create an account to track stats across days."
                : "Login to play and track your streaks."}
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}
