# Smart Home Floor Plan UI - Visual Reference

## UI Layout Overview

```
┌─────────────────────────────────────────────────────────────────┐
│  🏠 Smart Home Floor Plan                                       │
│  [+ Add Room] [All ON] [All OFF] [Refresh]                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              House Floor Plan                            │   │
│  │                                                           │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐│   │
│  │  │🚪 Living  │  │🚪 Bedroom │  │🚪 Kitchen │  │🚪 Entrance│   │
│  │  │  Room     │  │           │  │           │  │           │   │
│  │  │ 4 devices │  │ 3 devices │  │ 2 devices │  │ 3 devices │   │
│  │  │           │  │           │  │           │  │           │   │
│  │  │ 💡 🔊     │  │ 💡 🌡️    │  │ 💡 🔌    │  │ 🔒 📷    │   │
│  │  │ 📺 👁️    │  │ 🔌        │  │           │  │ 💡        │   │
│  │  │           │  │           │  │           │  │           │   │
│  │  │[+Add Dev] │  │[+Add Dev] │  │[+Add Dev] │  │[+Add Dev] │   │
│  │  └──────────┘  └──────────┘  └──────────┘  └──────────┘│   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                  │
├─────────────────────────────────────────────────────────────────┤
│  ⚡ Total Energy Consumption: 45.50 kWh                         │
└─────────────────────────────────────────────────────────────────┘
```

## Device Icons Reference

| Device Type    | Icon | Color      | Controls Available                    |
|----------------|------|------------|---------------------------------------|
| Light          | 💡   | Orange     | ON/OFF, Brightness slider (0-100%)    |
| Thermostat     | 🌡️   | Red        | ON/OFF, Temperature slider (10-35°C)  |
| Smart TV       | 📺   | Purple     | ON/OFF, Volume slider (0-100%)        |
| Smart Plug     | 🔌   | Purple     | ON/OFF toggle                         |
| Smart Lock     | 🔒   | Dark Gray  | Lock/Unlock button                    |
| Camera         | 📷   | Teal       | ON/OFF, Start/Stop recording          |
| Motion Sensor  | 👁️   | Green      | Simulate motion, Clear motion         |
| Speaker        | 🔊   | Blue       | ON/OFF, Play/Pause, Volume slider     |

## Room Color Palette

Available colors when creating a new room:

```
┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐
│ 1  │ 2  │ 3  │ 4  │ 5  │ 6  │ 7  │ 8  │ 9  │ 10 │
├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤
│🟠  │🟣  │🟢  │🟡  │🔴  │🔵  │🩷  │🟤  │🟡  │🟠  │
│Peach│Lav │Mint│Cream│Pink│Cyan│Rose│Beige│Khaki│Wheat│
└────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘
```

## Device Control Dialog Examples

### Light Control Dialog
```
┌─────────────────────────────────┐
│  💡 Living Room Light           │
├─────────────────────────────────┤
│  Status: ON (Brightness: 75%)   │
│                                  │
│  [Turn OFF]                      │
│                                  │
│  Brightness                      │
│  ├────────●──────┤ 75%          │
│  0              100              │
│                                  │
│  [Close]                         │
└─────────────────────────────────┘
```

### Thermostat Control Dialog
```
┌─────────────────────────────────┐
│  🌡️ Bedroom Thermostat          │
├─────────────────────────────────┤
│  Status: ON (Target: 22.0°C)    │
│                                  │
│  [Turn OFF]                      │
│                                  │
│  Target Temperature              │
│  ├──────●────────┤ 22°C         │
│  10            35                │
│                                  │
│  [Close]                         │
└─────────────────────────────────┘
```

### Smart Lock Control Dialog
```
┌─────────────────────────────────┐
│  🔒 Front Door                   │
├─────────────────────────────────┤
│  Status: LOCKED                  │
│                                  │
│  [Turn OFF]                      │
│                                  │
│  [🔓 Unlock]                     │
│                                  │
│  [Close]                         │
└─────────────────────────────────┘
```

### Camera Control Dialog
```
┌─────────────────────────────────┐
│  📷 Security Camera              │
├─────────────────────────────────┤
│  Status: STANDBY (1080p)         │
│                                  │
│  [Turn OFF]                      │
│                                  │
│  [⏺ Start Recording]             │
│                                  │
│  [Close]                         │
└─────────────────────────────────┘
```

### Speaker Control Dialog
```
┌─────────────────────────────────┐
│  🔊 Smart Speaker                │
├─────────────────────────────────┤
│  Status: PLAYING (Vol: 60%)      │
│                                  │
│  [Turn OFF]                      │
│                                  │
│  [⏸ Pause]                       │
│                                  │
│  Volume                          │
│  ├────────●──────┤ 60%          │
│  0              100              │
│                                  │
│  [Close]                         │
└─────────────────────────────────┘
```

## Add Room Dialog
```
┌─────────────────────────────────┐
│  Add New Room                    │
├─────────────────────────────────┤
│  Room Name:                      │
│  [Bathroom____________]          │
│                                  │
│  Room Color:                     │
│  ┌──┬──┬──┬──┬──┐               │
│  │🟠│🟣│🟢│🟡│🔴│               │
│  └──┴──┴──┴──┴──┘               │
│  ┌──┬──┬──┬──┬──┐               │
│  │🔵│🩷│🟤│🟡│🟠│               │
│  └──┴──┴──┴──┴──┘               │
│                                  │
│  [Add Room]  [Cancel]            │
└─────────────────────────────────┘
```

## Add Device Dialog
```
┌─────────────────────────────────┐
│  Add Device to Bathroom          │
├─────────────────────────────────┤
│  Device Type:                    │
│  [Light            ▼]            │
│   - Light                        │
│   - Thermostat                   │
│   - Smart TV                     │
│   - Smart Plug                   │
│   - Smart Lock                   │
│   - Camera                       │
│   - Motion Sensor                │
│   - Speaker                      │
│                                  │
│  Device Name:                    │
│  [Bathroom Light___]             │
│                                  │
│  [Add Device]  [Cancel]          │
└─────────────────────────────────┘
```

## Energy Dashboard States

### Low Consumption (< 25 kWh) - GREEN
```
┌─────────────────────────────────┐
│  ⚡ Total Energy Consumption     │
│     15.50 kWh                    │
│     (Green color)                │
└─────────────────────────────────┘
```

### Medium Consumption (25-50 kWh) - ORANGE
```
┌─────────────────────────────────┐
│  ⚡ Total Energy Consumption     │
│     35.75 kWh                    │
│     (Orange color)               │
└─────────────────────────────────┘
```

### High Consumption (> 50 kWh) - RED
```
┌─────────────────────────────────┐
│  ⚡ Total Energy Consumption     │
│     67.20 kWh                    │
│     (Red color)                  │
└─────────────────────────────────┘
```

## Hover Effects

### Room Tile Hover
```
Before Hover:                After Hover:
┌──────────┐                ┌──────────┐
│🚪 Living  │                │🚪 Living  │ ← Blue border
│  Room     │    ──────>     │  Room     │ ← Larger shadow
│ 4 devices │                │ 4 devices │ ← Slightly scaled up
└──────────┘                └──────────┘
```

### Device Icon Hover
```
Before Hover:                After Hover:
   💡                           💡
Living Room Light            ┌─────────────────┐
                             │Living Room Light│ ← Tooltip
                             │Status: ON       │
                             │Brightness: 100% │
                             └─────────────────┘
```

### Button Hover
```
Before Hover:                After Hover:
[+ Add Room]                 [+ Add Room] ← Darker background
                                          ← Slightly scaled up
                                          ← Hand cursor
```

## Device State Indicators

### Device ON
```
💡  ← Full opacity (1.0)
    Bright, fully visible
```

### Device OFF
```
💡  ← Reduced opacity (0.4)
    Dimmed, semi-transparent
```

## Responsive Layout

### Large Window (1400x900)
```
┌────────────────────────────────────────────────┐
│  [4 rooms in a row]                            │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐             │
│  │Room1│ │Room2│ │Room3│ │Room4│             │
│  └─────┘ └─────┘ └─────┘ └─────┘             │
└────────────────────────────────────────────────┘
```

### Medium Window (1000x700)
```
┌────────────────────────────────┐
│  [2 rooms per row]             │
│  ┌─────┐ ┌─────┐               │
│  │Room1│ │Room2│               │
│  └─────┘ └─────┘               │
│  ┌─────┐ ┌─────┐               │
│  │Room3│ │Room4│               │
│  └─────┘ └─────┘               │
└────────────────────────────────┘
```

### Small Window (800x600)
```
┌──────────────────┐
│  [1 room per row]│
│  ┌─────┐         │
│  │Room1│         │
│  └─────┘         │
│  ┌─────┐         │
│  │Room2│         │
│  └─────┘         │
│  ┌─────┐         │
│  │Room3│         │
│  └─────┘         │
│  [Scroll...]     │
└──────────────────┘
```

## Color Scheme Reference

### Primary Colors
- **Background**: #f5f7fa (Light gray-blue)
- **Top Bar**: #2c3e50 → #34495e (Dark gradient)
- **Room Tiles**: White (#FFFFFF)
- **Borders**: #95a5a6 (Gray)

### Device Icon Colors
- **Light**: #f39c12 (Orange)
- **Thermostat**: #e74c3c (Red)
- **TV**: #8e44ad (Purple)
- **Plug**: #9b59b6 (Purple)
- **Lock**: #34495e (Dark gray)
- **Camera**: #16a085 (Teal)
- **Sensor**: #27ae60 (Green)
- **Speaker**: #2980b9 (Blue)

### Button Colors
- **Add Button**: #2ecc71 (Green)
- **Control Button**: #3498db (Blue)
- **Toggle ON**: #27ae60 (Green)
- **Toggle OFF**: #95a5a6 (Gray)

### Energy Colors
- **Low**: #27ae60 (Green)
- **Medium**: #f39c12 (Orange)
- **High**: #e74c3c (Red)

## Typography

- **Title**: 28px, Bold, White
- **Room Name**: 16px, Bold, Dark gray
- **Device Name**: 12px, Bold, Dark gray
- **Status Text**: 10-14px, Regular, Gray
- **Button Text**: 11-14px, Regular, White

## Spacing & Sizing

- **Room Tile**: 280px × 320px
- **Device Icon**: 45px × 45px (circular)
- **Gap between rooms**: 15px
- **Padding**: 10-20px (varies by component)
- **Border Radius**: 5-10px (varies by component)
- **Shadow**: 0-12px blur (varies by component)

## Animation Timings

- **Hover scale**: 1.02-1.05x
- **Transition duration**: 200-300ms
- **Ease function**: ease-in-out

---

This visual reference provides a comprehensive overview of the UI layout, components, and styling. Use it as a guide when testing or customizing the application.
