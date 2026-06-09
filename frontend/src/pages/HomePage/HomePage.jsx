import { CalendarDays, HeartHandshake, BrainIcon } from "lucide-react";
import { Link } from "react-router-dom";
import "./HomePageStyle.css";

const features = [
  {
    icon: HeartHandshake,
    title: "Acolhimento especializado",
    text: "Atendimento humanizado para mulheres em situação de violência."
  },
  {
    icon: BrainIcon,
    title: "Apoio psicológico",
    text: "Atendimento psicológico individual para apoio emocional e fortalecimento do bem-estar."
  },
  {
    icon: CalendarDays,
    title: "Agendamento online",
    text: "Solicite seu horário de forma simples, sem necessidade de cadastro prévio."
  }
];

export default function HomePage() {
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
            <Icon className="feature-icon" size={64} />
            <h2 className="feature-title">{title}</h2>
            <p className="feature-text">{text}</p>
          </article>
        ))}
      </div>
    </section>
  );
}