package com.Aaeli.jinun;

import com.Aaeli.jinun.commands.CommandManager;
import com.Aaeli.jinun.commands.impl.*;
import com.Aaeli.jinun.core.event.EventBus;
import com.Aaeli.jinun.core.event.Priority;
import com.Aaeli.jinun.core.scheduler.TaskScheduler;
import com.Aaeli.jinun.engine.ai.EntityTracker;
import com.Aaeli.jinun.engine.ai.PredictionEngine;
import com.Aaeli.jinun.engine.ai.TargetSelector;
import com.Aaeli.jinun.engine.combat.AttackEngine;
import com.Aaeli.jinun.engine.combat.CriticalEngine;
import com.Aaeli.jinun.engine.network.PacketPipeline;
import com.Aaeli.jinun.engine.rotation.RotationEngine;
import com.Aaeli.jinun.engine.state.GameState;
import com.Aaeli.jinun.events.EventPacket;
import com.Aaeli.jinun.events.EventTickStart;
import com.Aaeli.jinun.managers.*;
import com.Aaeli.jinun.ui.hud.HudManager;
import com.Aaeli.jinun.ui.hud.impl.ArrayListHud;

public class JinunContext {
    // Core
    public final EventBus       eventBus       = new EventBus();
    public final TaskScheduler  scheduler      = new TaskScheduler();
    public final KeybindManager keybindManager = new KeybindManager();

    // State
    public final GameState gameState = new GameState();

    // AI
    public final EntityTracker    entityTracker  = new EntityTracker();
    public final TargetSelector   targetSelector = new TargetSelector();
    public final PredictionEngine prediction     = new PredictionEngine();

    // Combat
    public final AttackEngine   attackEngine   = new AttackEngine();
    public final CriticalEngine criticalEngine = new CriticalEngine();

    // Rotation & Network
    public final RotationEngine rotationEngine = new RotationEngine();
    public final PacketPipeline packetPipeline = new PacketPipeline(this);

    // Managers
    public final FriendManager  friendManager  = new FriendManager();
    public final CommandManager commandManager = new CommandManager();
    public final HudManager     hudManager     = new HudManager();
    public final ModuleManager  moduleManager  = new ModuleManager(this);
    public final ConfigManager  configManager  = new ConfigManager(this);

    public void init() {
        // Friend → TargetSelector
        targetSelector.init(friendManager);

        moduleManager.init();
        configManager.load();

        // Commands
        commandManager.register(new HelpCommand(this));
        commandManager.register(new ToggleCommand(this));
        commandManager.register(new BindCommand(this));
        commandManager.register(new FriendCommand(this));

        // HUD
        hudManager.register(new ArrayListHud());

        // Event bindings
        eventBus.bind(EventTickStart.ID, Priority.CRITICAL,
                (EventTickStart e) -> gameState.update());
        eventBus.bind(EventTickStart.ID, Priority.HIGH,
                (EventTickStart e) -> scheduler.tick());
        eventBus.bind(EventTickStart.ID, Priority.HIGH,
                entityTracker::onTick);
        eventBus.bind(EventTickStart.ID, Priority.HIGH,
                attackEngine::onTick);
        eventBus.bind(EventPacket.ID, packetPipeline::onPacket);
    }
}