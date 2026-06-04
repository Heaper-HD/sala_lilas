import { CalendarDays, HeartHandshake, Shield } from "lucide-react";
import { Link } from "react-router-dom";

const features = [
  {
    icon: HeartHandshake,
    title: "Acolhimento especializado",
    text: "Atendimento humanizado para mulheres em situação de violência."
  },
  {
    icon: Shield,
    title: "Sigilo e proteção",
    text: "Seus dados são tratados com segurança, em conformidade com a LGPD."
  },
  {
    icon: CalendarDays,
    title: "Agendamento online",
    text: "Solicite seu horário de forma simples, sem necessidade de cadastro prévio."
  }
];

export default function HomePage() {
  return (
    <section className="space-y-10">
      <div className="rounded-2xl bg-gradient-to-br from-purple-600 to-purple-800 px-6 py-12 text-white shadow-lg sm:px-10">
        <h1 className="text-3xl font-bold sm:text-4xl">Sala Lilás</h1>
        <p className="mt-4 max-w-2xl text-base text-purple-100 sm:text-lg">
          Espaço de acolhimento, escuta e encaminhamento para mulheres em
          situação de violência. Você não precisa enfrentar isso sozinha.
        </p>
        <div className="mt-8 flex flex-wrap gap-3">
          <Link
            to="/agendamento"
            className="rounded-lg bg-white px-5 py-2.5 text-sm font-semibold text-purple-700 transition hover:bg-purple-50"
          >
            Agendar atendimento
          </Link>
          <Link
            to="/login"
            className="rounded-lg border border-purple-200 px-5 py-2.5 text-sm font-semibold text-white transition hover:bg-purple-700"
          >
            Área da equipe
          </Link>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        {features.map(({ icon: Icon, title, text }) => (
          <article
            key={title}
            className="rounded-xl border border-purple-100 bg-white p-5 shadow-sm"
          >
            <Icon className="text-purple-600" size={24} />
            <h2 className="mt-3 font-semibold text-slate-800">{title}</h2>
            <p className="mt-2 text-sm text-slate-600">{text}</p>
          </article>
        ))}
      </div>
    </section>
  );
}
