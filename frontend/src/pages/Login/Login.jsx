import { LockKeyhole, Mail } from "lucide-react";
import { useState } from "react";
import toast from "react-hot-toast";
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import "./LoginStyle.css";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from || "/painel";

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!email.trim() || !password.trim()) {
      toast.error("Por favor, preencha todos os campos");
      return;
    }

    setSubmitting(true);
    try {
      await login(email.trim(), password);
      toast.success("Login realizado com sucesso!");
      navigate(from, { replace: true });
    } catch (error) {
      toast.error(error.message || "Falha no login. Verifique suas credenciais.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <section className="login-container">
      <div className="login-card">
        <div className="login-header">
          <h1 className="login-title">Acessar conta</h1>
          <p className="login-subtitle">
            Entre para continuar no sistema Sala Lilás
          </p>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>
          <label className="form-group">
            <span className="form-label">
              E-mail
            </span>
            <div className="input-wrapper">
              <Mail className="input-icon" size={18} />
              <input
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="voce@exemplo.com"
                autoComplete="email"
                className="form-control"
              />
            </div>
          </label>

          <label className="form-group">
            <span className="form-label">
              Senha
            </span>
            <div className="input-wrapper">
              <LockKeyhole className="input-icon" size={18} />
              <input
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="Digite sua senha"
                autoComplete="current-password"
                className="form-control"
              />
            </div>
          </label>

          <button
            type="submit"
            disabled={submitting}
            className="btn-submit"
          >
            {submitting ? "Entrando..." : "Entrar"}
          </button>
        </form>
      </div>
    </section>
  );
}