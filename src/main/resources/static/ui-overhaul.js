(() => {
  const prefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (!prefersReduced) {
    document.querySelectorAll('.glass-card, .card, .panel, .event-card, tr').forEach((el, idx) => {
      el.animate(
        [{ opacity: 0, transform: 'translateY(12px)' }, { opacity: 1, transform: 'translateY(0)' }],
        { duration: 320 + idx * 30, easing: 'cubic-bezier(0.16,1,0.3,1)', fill: 'both' }
      );
    });
  }

  document.querySelectorAll('button, .btn-primary, a.btn').forEach(btn => {
    btn.setAttribute('aria-live', 'polite');
  });
})();
