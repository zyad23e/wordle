import { useEffect, useState } from "react";

const API_BASE = import.meta.env.VITE_API_URL ?? "http://localhost:8080";


export default function Stats({ onClose }) {
  const [stats, setStats] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setError("");
      try {
        const res = await fetch(`${API_BASE}/api/stats`, {
        });

        if (!res.ok) {
          if (!cancelled) setError(`Stats request failed (HTTP ${res.status})`);
          return;
        }

        const data = await res.json();
        if (!cancelled) setStats(data);
      } catch (e) {
        if (!cancelled) setError("Network/CORS error while loading stats");
      }
    }

    load();
    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <div
      style={{
        position: "fixed",
        inset: 0,
        background: "rgba(0,0,0,0.6)",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontFamily: "system-ui",
      }}
    >
      <div
        style={{
          background: "#151515",
          padding: 20,
          borderRadius: 12,
          color: "white",
          minWidth: 300,
          border: "1px solid #333",
        }}
      >
        <h3 style={{ marginTop: 0 }}>Stats</h3>

        {error && (
          <div
            style={{
              padding: "8px 10px",
              borderRadius: 10,
              background: "#2a1414",
              border: "1px solid #5a1f1f",
              color: "#ffb3b3",
              fontWeight: 700,
              marginBottom: 12,
            }}
          >
            {error}
          </div>
        )}

        {!error && !stats && <div>Loading stats…</div>}

        {stats && (
          <>
            <div>Games played: {stats.gamesPlayed}</div>
            <div>Wins: {stats.wins}</div>
            <div>Current streak: {stats.currentStreak}</div>
            <div>Max streak: {stats.maxStreak}</div>

            <h4 style={{ marginBottom: 8 }}>Win distribution</h4>
            <div>1: {stats.winIn1}</div>
            <div>2: {stats.winIn2}</div>
            <div>3: {stats.winIn3}</div>
            <div>4: {stats.winIn4}</div>
            <div>5: {stats.winIn5}</div>
            <div>6: {stats.winIn6}</div>
          </>
        )}

        <button
          onClick={onClose}
          style={{
            marginTop: 14,
            padding: "8px 12px",
            borderRadius: 10,
            border: "1px solid #444",
            background: "#1b1b1b",
            color: "white",
            cursor: "pointer",
            fontWeight: 800,
            width: "100%",
          }}
        >
          Close
        </button>
      </div>
    </div>
  );
}
