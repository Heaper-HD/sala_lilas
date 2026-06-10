import { Link } from "react-router-dom";
import "./HomePageStyle.css";

const features = [
  {
    icon: "🤝",
    title: "Acolhimento",
    text: "Escuta qualificada e acolhimento humanizado por equipe especializada em situações de violência.",
  },
  {
    icon: "🧠",
    title: "Apoio Psicológico",
    text: "Atendimento psicológico individual para apoio emocional e fortalecimento do bem-estar.",
  },
  {
    icon: "⚖️",
    title: "Orientação Jurídica",
    text: "Informação sobre direitos, medidas protetivas e encaminhamentos para a Defensoria Pública.",
  }
];

export default function HomePage()
{
  return (
    <section className="home-section">
      <div className="hero-banner">
        <h1 className="hero-title">Sala Lilás</h1>
        <p className="hero-text">
          Espaço de acolhimento, escuta e encaminhamento para mulheres em
          situação de violência. Você não precisa enfrentar isso sozinha.
        </p>
        <div className="hero-actions">
          <Link
            to="/agendamento"
            className="btn btn-primary"
          >
            Agendar atendimento
          </Link>
          <Link
            to="/login"
            className="btn btn-secondary"
          >
            Área da equipe
          </Link>
        </div>
      </div>

      <div className="features-grid">
        {features.map(({ icon: Icon, title, text }) => (
          <article
            key={title}
            className="feature-card"
          >
            <h2 className="feature-icon" size={64} >
              {Icon}
            </h2>
            <h2 className="feature-title">{title}</h2>
            <p className="feature-text">{text}</p>
          </article>
        ))}
      </div>
    </section>
  );
}