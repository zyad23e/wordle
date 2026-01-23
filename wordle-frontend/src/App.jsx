import { useEffect, useRef, useState } from "react";
import Stats from "./Stats";

const API_BASE = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

function statusToStyle(status) {
  if (status === "CORRECT") return { background: "#2e7d32", color: "white" };
  if (status === "PRESENT") return { background: "#f9a825", color: "white" };
  if (status === "ABSENT") return { background: "#424242", color: "white" };
  return {};
}

export default function App() {
  const [puzzle, setPuzzle] = useState(null);
  const [error, setError] = useState("");

  const [guesses, setGuesses] = useState([]);
  const [evaluations, setEvaluations] = useState([]);
  const [currentRow, setCurrentRow] = useState(0);

  const [currentGuess, setCurrentGuess] = useState("");
  const [cursorLen, setCursorLen] = useState(0);

  const [message, setMessage] = useState("");
  const [gameOver, setGameOver] = useState(false);
  const [showStats, setShowStats] = useState(false);

  // refs to avoid stale closures
  const puzzleRef = useRef(null);
  const colsRef = useRef(5);
  const rowsRef = useRef(6);
  const currentRowRef = useRef(0);
  const currentGuessRef = useRef("");
  const cursorLenRef = useRef(0);
  const gameOverRef = useRef(false);

  useEffect(() => { currentRowRef.current = currentRow; }, [currentRow]);
  useEffect(() => { currentGuessRef.current = currentGuess; }, [currentGuess]);
  useEffect(() => { cursorLenRef.current = cursorLen; }, [cursorLen]);
  useEffect(() => { gameOverRef.current = gameOver; }, [gameOver]);

  function updateGuess(next) {
    currentGuessRef.current = next;
    setCurrentGuess(next);
    cursorLenRef.current = next.length;
    setCursorLen(next.length);
  }

  async function loadGame() {
    try {
      const res = await fetch(`${API_BASE}/api/puzzle`, {
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();

      setPuzzle(data);
      puzzleRef.current = data;

      colsRef.current = data.length;
      rowsRef.current = data.maxAttempts;

      setGuesses(Array.from({ length: data.maxAttempts }, () => ""));
      setEvaluations(
        Array.from({ length: data.maxAttempts }, () =>
          Array(data.length).fill(null)
        )
      );

      setCurrentRow(0);
      currentRowRef.current = 0;
      updateGuess("");
      setMessage("");
      setGameOver(false);
      gameOverRef.current = false;
      setError("");
    } catch (e) {
      setError(String(e));
    }
  }

  useEffect(() => {
    loadGame();
  }, []);

  async function submitGuess() {
    if (!puzzleRef.current || gameOverRef.current) return;

    const cols = colsRef.current;
    const guess = currentGuessRef.current.toUpperCase();

    if (guess.length < cols) {
      setMessage("Not enough letters");
      return;
    }

    try {
      const res = await fetch(`${API_BASE}/api/guess`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          puzzleId: puzzleRef.current.puzzleId,
          guess,
          attempt: currentRowRef.current,
        }),
      });

      const data = await res.json();

      if (!data.isValidWord) {
        setMessage("Not in word list");
        return;
      }

      const rowIdx = currentRowRef.current;

      setGuesses((prev) => {
        const next = [...prev];
        next[rowIdx] = guess;
        return next;
      });

      setEvaluations((prev) => {
        const next = prev.map((r) => [...r]);
        next[rowIdx] = data.statuses;
        return next;
      });

      if (data.isWin) {
        setMessage("You got it 🎉");
        setGameOver(true);
        gameOverRef.current = true;
        return;
      }

      if (data.isGameOver) {
        setMessage("Game over");
        setGameOver(true);
        gameOverRef.current = true;
        return;
      }

      setCurrentRow(rowIdx + 1);
      currentRowRef.current = rowIdx + 1;
      updateGuess("");
      setMessage("");
    } catch {
      setMessage("Network error");
    }
  }

  useEffect(() => {
    const onKeyDown = (e) => {
      if (!puzzleRef.current || gameOverRef.current) return;
      if (e.ctrlKey || e.metaKey || e.altKey) return;

      const key = e.key;

      if (key === "Backspace") {
        e.preventDefault();
        updateGuess(currentGuessRef.current.slice(0, -1));
        return;
      }

      if (key === "Enter") {
        e.preventDefault();
        submitGuess();
        return;
      }

      if (/^[a-zA-Z]$/.test(key)) {
        e.preventDefault();
        if (currentGuessRef.current.length >= colsRef.current) return;
        updateGuess(currentGuessRef.current + key.toUpperCase());
      }
    };

    window.addEventListener("keydown", onKeyDown);
    return () => window.removeEventListener("keydown", onKeyDown);
  }, []);

  async function logout() {
    await fetch(`${API_BASE}/api/auth/logout`, {
      method: "POST",
    });
    window.location.reload();
  }

  if (error) {
    return <div style={{ padding: 24 }}>Error: {error}</div>;
  }

  if (!puzzle) {
    return <div style={{ padding: 24 }}>Loading…</div>;
  }

  const rows = puzzle.maxAttempts;
  const cols = puzzle.length;
  const cursorIndex =
    cursorLenRef.current < cols ? cursorLenRef.current : cols - 1;

  return (
    <div
      style={{
        padding: 24,
        background: "#111",
        minHeight: "100vh",
        color: "white",
        fontFamily: "system-ui",
      }}
    >
      <h1>Wordle Web</h1>

      <div style={{ marginBottom: 12 }}>
        <button onClick={loadGame}>Restart</button>
        <button onClick={() => setShowStats(true)} style={{ marginLeft: 8 }}>
          Stats
        </button>
        <button onClick={logout} style={{ marginLeft: 8 }}>
          Logout
        </button>
      </div>

      {message && <div style={{ marginBottom: 12 }}>{message}</div>}

      <div
        style={{
          display: "grid",
          gridTemplateRows: `repeat(${rows}, 1fr)`,
          gap: 8,
          width: cols * 52,
        }}
      >
        {Array.from({ length: rows }).map((_, r) => {
          const rowWord =
            r < currentRow
              ? guesses[r]
              : r === currentRow
              ? currentGuess.padEnd(cols)
              : "".padEnd(cols);

          return (
            <div
              key={r}
              style={{
                display: "grid",
                gridTemplateColumns: `repeat(${cols}, 1fr)`,
                gap: 8,
              }}
            >
              {Array.from({ length: cols }).map((_, c) => {
                const letter = rowWord[c] || "";
                const status = evaluations[r]?.[c];
                const cursor =
                  r === currentRow &&
                  c === cursorIndex &&
                  !gameOverRef.current;

                return (
                  <div
                    key={c}
                    style={{
                      width: 48,
                      height: 48,
                      border: "2px solid #444",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      fontSize: 24,
                      fontWeight: 800,
                      borderRadius: 8,
                      ...(cursor
                        ? { boxShadow: "0 0 0 2px white inset" }
                        : {}),
                      ...statusToStyle(status),
                    }}
                  >
                    {letter}
                  </div>
                );
              })}
            </div>
          );
        })}
      </div>

      {showStats && <Stats onClose={() => setShowStats(false)} />}
    </div>
  );
}
