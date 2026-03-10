# Design System: 智慧物业业主端

## Pattern
- **Name:** Hero-Centric + Feature-Rich
- **Conversion Focus:** Trust and authority with property showcase
- **CTA Placement:** Above fold and section-integrated
- **Color Strategy:** Trust Blue + Gold accents for premium feel
- **Sections:** Hero > Property Showcase > Services > Testimonials > Contact

## Style
- **Name:** Glassmorphism + Minimalism
- **Keywords:** Clean, Modern, Trust-focused, Property-oriented
- **Best For:** Real estate and property management platforms
- **Performance:** High | **Accessibility:** WCAG AA compliant

## Colors
| Role | Hex | CSS Variable |
|------|-----|--------------|
| Primary | `#2563EB` | `--color-primary` |
| Secondary | `#3B82F6` | `--color-secondary` |
| CTA | `#F59E0B` | `--color-cta` |
| Background | `#F8FAFC` | `--color-background` |
| Text | `#1E293B` | `--color-text` |

*Notes: Trust blue for professional appearance, amber/gold for premium accent*

## Typography
- **Heading:** Inter
- **Body:** Inter
- **Mood:** Professional, Clean, Trustworthy
- **Best For:** Property management dashboards and service platforms
- **Google Fonts:** [Inter](https://fonts.google.com/specimen/Inter)
- **CSS Import:**
```css
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
```

## Avoid (Anti-patterns)
- ❌ Excessive animation
- ❌ Dark mode by default
- ❌ Complex onboarding flow
- ❌ Cluttered layout
- ❌ Missing trust indicators

### Additional Forbidden Patterns
- ❌ **Emojis as icons** — Use SVG icons (Heroicons, Lucide, Simple Icons)
- ❌ **Missing cursor:pointer** — All clickable elements must have cursor:pointer
- ❌ **Layout-shifting hovers** — Avoid scale transforms that shift layout
- ❌ **Low contrast text** — Maintain 4.5:1 minimum contrast ratio
- ❌ **Instant state changes** — Always use transitions (150-300ms)
- ❌ **Invisible focus states** — Focus states must be visible for a11y

## Pre-Delivery Checklist
- [ ] No emojis used as icons (use SVG instead)
- [ ] All icons from consistent icon set (Heroicons/Lucide)
- [ ] `cursor-pointer` on all clickable elements
- [ ] Hover states with smooth transitions (150-300ms)
- [ ] Light mode: text contrast 4.5:1 minimum
- [ ] Focus states visible for keyboard navigation
- [ ] `prefers-reduced-motion` respected
- [ ] Responsive: 375px, 768px, 1024px, 1440px
- [ ] No content hidden behind fixed navbars
- [ ] No horizontal scroll on mobile
