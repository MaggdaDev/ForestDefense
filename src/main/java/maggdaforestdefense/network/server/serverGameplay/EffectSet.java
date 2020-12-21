/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import com.google.gson.Gson;
import java.util.Vector;

/**
 *
 * @author DavidPrivat
 */
public class EffectSet {

    private Vector<Effect> effects, effectsToRemove;

    public EffectSet() {
        effects = new Vector<>();
        effectsToRemove = new Vector<>();
    }
    
    public void update(double timeElapsed) {
        for(Effect effect: effects) {
            effect.update(timeElapsed);
            if(!effect.isActive) {
                effectsToRemove.add(effect);
            }
        }
        effects.removeAll(effectsToRemove);
        effectsToRemove.clear();
    }
    
    public boolean isActive(EffectType type) {
        Effect e = searchForType(type);
        if(e == null || (!e.isActive)) {
            return false;
        }
        return true;
    }

    public void addEffect(Effect effect) {
        Effect existingEffect = searchForType(effect.getType());
        if (existingEffect != null && existingEffect.getRestTime() > effect.getRestTime()) {
            return;
        }

        effects.add(effect);
    }
    
    public void removeEffect(EffectType type) {
        Effect e = searchForType(type);
        if(e != null) {
            e.setActive(false);
        }
    }

    public Effect searchForType(EffectType type) {
        for (Effect effect : effects) {
            if (effect.getType().equals(type)) {
                return effect;
            }
        }
        return null;
    }

    public void forEachEffect(EffectHandler handler) {
        effects.forEach((Effect effect) -> {
            if (effect.isActive()) {
                handler.handleEffect(effect);
            } 
        });
    }
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    
    public static EffectSet fromString(String s) {
        return new Gson().fromJson(s, EffectSet.class);
    }

    public static enum EffectType {
        // MOBS
        STUN,
        SLOW,
        SENSITIVE,
        GOLDED,
        //
        MAPLE_ESCALATION,
        MAPLE_CHARGED
    }

    public static interface EffectHandler {

        public void handleEffect(Effect effect);
    }

    public static class Effect {
        public static double EFFECT_SENSITIVE_MULT = 2;
        
        
        public static double UNLIMITED = -1.0d;

        private double duration;
        private double timer = 0;
        private boolean isActive = true;
        private EffectType type;

        public Effect(EffectType effectType, double duration) {
            this.type = effectType;
            this.duration = duration;
        }

        public void update(double timeElapsed) {
            if (duration > 0) {
                timer += timeElapsed;
                if (timer > duration) {
                    isActive = false;
                }
            }
        }
        
        public void setActive(boolean b) {
            isActive = b;
        }

        public double getDuration() {
            return duration;
        }

        public double getRestTime() {
            return duration - timer;
        }

        public boolean isActive() {
            return isActive;
        }

        public EffectType getType() {
            return type;
        }
    }
}
