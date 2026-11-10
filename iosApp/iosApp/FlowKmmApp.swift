import SwiftUI
import shared

@main
struct FlowKmmApp: App {
    init() {
        // Initialize Koin from the shared module
        // KoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    var body: some View {
        NavigationStack {
            VStack(spacing: 24) {
                Image(systemName: "checkmark.circle.fill")
                    .font(.system(size: 64))
                    .foregroundColor(.blue)

                Text("Flow KMM")
                    .font(.largeTitle)
                    .fontWeight(.bold)

                Text("Habit tracking powered by shared Kotlin logic.")
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)

                // TODO: Wire up shared module ViewModels via KMP interop
                Text("iOS UI coming soon")
                    .font(.caption)
                    .foregroundColor(.tertiary)
            }
            .navigationTitle("Habits")
        }
    }
}

#Preview {
    ContentView()
}
