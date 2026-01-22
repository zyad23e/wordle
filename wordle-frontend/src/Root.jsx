import { useEffect, useState } from "react";
import App from "./App";
import Login from "./Login";

const API_BASE = import.meta.env.VITE_API_BASE ?? "http://localhost:8080";

export default function Root() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    let cancelled = false;

    async function checkSession() {
      try {
        const res = await fetch(`${API_BASE}/api/me`, {
          credentials: "include",
        });

        if (cancelled) return;

        if (res.ok) {
          setLoggedIn(true);
        } else {
          // 401/403/etc => not logged in
          setLoggedIn(false);
        }
      } catch (e) {
        if (!cancelled) setLoggedIn(false);
      } finally {
        if (!cancelled) setChecking(false);
      }
    }

    checkSession();

    return () => {
      cancelled = true;
    };
  }, []);

  if (checking) {
    return (
      <div style={{ padding: 24, fontFamily: "system-ui" }}>
        Loading…
        <div style={{ marginTop: 8, opacity: 0.7, fontSize: 12 }}>
          If this hangs, your backend likely blocked /api/me due to CORS or it’s not running.
        </div>
      </div>
    );
  }

  if (!loggedIn) {
    return <Login onLoginSuccess={() => setLoggedIn(true)} />;
  }

  return <App />;
}
