export default function LoadingSpinner({ fullPage = false, label = "Carregando..." }) {
  const wrapper = fullPage
    ? "flex min-h-[40vh] items-center justify-center"
    : "flex items-center justify-center py-8";

  return (
    <div className={wrapper} role="status" aria-live="polite">
      <div className="flex flex-col items-center gap-3">
        <div className="h-8 w-8 animate-spin rounded-full border-2 border-purple-200 border-t-purple-600" />
        <span className="text-sm text-slate-600">{label}</span>
      </div>
    </div>
  );
}
