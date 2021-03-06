package com.lss233.phoenix.spigot.utils.spigot;

import com.lss233.phoenix.Phoenix;
import com.lss233.phoenix.block.Block;
import com.lss233.phoenix.entity.Entity;
import com.lss233.phoenix.entity.EntityType;
import com.lss233.phoenix.entity.living.Player;
import com.lss233.phoenix.math.Vector;
import com.lss233.phoenix.world.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.lss233.phoenix.spigot.SpigotMain.getTransformer;

/**
 *
 */
public interface WorldTransform {
    /**
     * Convert a Bukkit World instance to a Phoenix World instance.
     * @param world The Bukkit World instance.
     * @return The Phoenix World instance.
     */
    default World toPhoenix(org.bukkit.World world){
        return new World() {
            /**
             * Create an entity instance at the given position.
             *
             * @param entityType The type
             * @param vector     The position
             * @return An entity
             */
            @Override
            public Optional<Entity> createEntity(EntityType entityType, Vector vector) {
                org.bukkit.entity.Entity entity = world.spawnEntity(getTransformer()
                            .toSpigot(vector.toLocation(getTransformer().toPhoenix(world))),
                        org.bukkit.entity.EntityType.valueOf(entityType.toString()));
                if(entity == null)
                    return Optional.empty();
                else
                    return Optional.of(getTransformer().toPhoenix(entity));
            }

            /**
             * Return a list of entities contained within {@code distance} blocks
             * of the specified location. This uses a sphere to test distances.
             *
             * @param location The location at the center of the search radius
             * @param distance The search radius
             * @return A list of nearby entities
             */
            @Override
            public List<Entity> getNearbyEntities(Vector location, double distance) {
                List<Entity> entities = new ArrayList<>();
                world.getNearbyEntities(getTransformer().toSpigot(location.toLocation(getTransformer().toPhoenix(world))), distance, distance, distance)
                        .forEach((entity)-> entities.add(getTransformer().toPhoenix(entity)));
                return entities;
            }

            /**
             * Return a list of entities contained within this world.
             *
             * @return A list of entities
             */
            @Override
            public List<Entity> getEntities() {
                List<Entity> entities = new ArrayList<>();
                world.getEntities().forEach((entity)-> entities.add(getTransformer().toPhoenix(entity)));
                return entities;
            }

            /**
             * Gets the entity whose {@link UUID} matches the provided id, possibly
             * returning no entity if the entity is not loaded or non-existent.
             *
             * @param uniqueId The unique id
             * @return An entity
             */
            @Override
            public Optional<Entity> getEntity(UUID uniqueId) {
                return Optional.empty();
            }

            @Override
            public String getName() {
                return world.getName();
            }

            @Override
            public List<Player> getPlayers() {
                List<com.lss233.phoenix.entity.living.Player> players = new ArrayList<>();
                for (org.bukkit.entity.Player player : world.getPlayers())
                    players.add(getTransformer().toPhoenix(player));
                return players;
            }

            @Override
            public UUID getUniqueId() {
                return world.getUID();
            }

            @Override
            public WorldProperties getProperties() {
                return toPhoenixWorldProperties(world);
            }

            @Override
            public boolean setBlock(Block block, boolean force) {
                throw new UnsupportedOperationException("Not implement yet.");
            }
        };
    }

    default WorldProperties toPhoenixWorldProperties(org.bukkit.World world){
        return new WorldProperties() {
            @Override
            public Difficulty getDifficulty() {
                return Difficulty.valueOf(world.getDifficulty().toString());
            }

            @Override
            public void setDifficulty(Difficulty difficulty) {
                world.setDifficulty(org.bukkit.Difficulty.valueOf(difficulty.toString()));
            }

            @Override
            public boolean isHardcore() {
                return Bukkit.isHardcore();
            }

            @Override
            public void setHardcore(boolean hardcore) {
                //TODO https://bukkit.org/threads/setting-server-to-hardcore-mode-possible.399990/
                Phoenix.getLogger("Transform").warn("WorldProperties#setHardcore is not available for now.");
            }

            @Override
            public long getSeed() {
                return world.getSeed();
            }

            @Override
            public void setSeed(long seed) {
                //TODO https://bukkit.org/threads/is-there-a-way-to-modify-a-worlds-seed.80069/
                Phoenix.getLogger("Transform").warn("WorldProperties#setSeed is not available for now.");
            }

            @Override
            public long getTotalTime() {
                return world.getFullTime();
            }

            @Override
            public long getWorldTime() {
                return world.getTime();
            }

            @Override
            public void setWorldTime(long time) {
                world.setTime(time);
            }

            @Override
            public int getThunderDuration() {
                return world.getThunderDuration();
            }

            @Override
            public void setThunderDuration(int thunderDuration) {
                world.setThunderDuration(thunderDuration);
            }

            @Override
            public boolean isThundering() {
                return world.isThundering();
            }

            @Override
            public void setThundering(boolean thundering) {
                world.setThundering(thundering);
            }

            @Override
            public int getRainDuration() {
                return world.getWeatherDuration();
            }

            @Override
            public void setRainDuration(int rainDuration) {
                world.setWeatherDuration(rainDuration);
            }

            @Override
            public boolean isRaining() {
                return world.hasStorm();
            }

            @Override
            public void setRaining(boolean raining) {
                world.setStorm(raining);
            }

            @Override
            public Optional<String> getGameRules(String gameRule) {
                return Optional.of(world.getGameRuleValue(gameRule));
            }

            @Override
            public void setGameRule(String key, String value) {
                world.setGameRuleValue(key, value);
            }

            @Override
            public Location getSpawnLocation() {
                return getTransformer().toPhoenix(world.getSpawnLocation());
            }

            @Override
            public void setSpawnLocation(Location spawnLocation) {
                org.bukkit.Location location = getTransformer().toSpigot(spawnLocation);
                world.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            }

            @Override
            public WorldBorder getWorldBorderCenter() {
                return getTransformer().toPhoenix(world.getWorldBorder());
            }
        };
    }
    default org.bukkit.World toSpigot(World world){
        return Bukkit.getWorld(world.getUniqueId());
    }
}
