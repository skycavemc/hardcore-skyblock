package de.leonheuer.skycave.hardcore.models

import com.google.common.io.Files
import com.google.common.io.Resources
import de.leonheuer.skycave.hardcore.annotations.CreateDataFolder
import de.leonheuer.skycave.hardcore.annotations.Prefix
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

abstract class SkyCavePlugin : JavaPlugin() {

    var prefix = ""
        private set

    override fun onEnable() {
        val clazz: Class<out SkyCavePlugin?> = this@SkyCavePlugin.javaClass
        if (clazz.isAnnotationPresent(CreateDataFolder::class.java)) {
            if (!dataFolder.isDirectory) {
                dataFolder.mkdirs()
            }
        }
        if (clazz.isAnnotationPresent(Prefix::class.java)) {
            prefix = clazz.getAnnotation(Prefix::class.java).value
        }
    }

    open fun registerCommand(command: String, executor: CommandExecutor?) {
        val cmd = getCommand(command)
        if (cmd == null) {
            logger.severe("No entry for the command $command found in the plugin.yml.")
            return
        }
        cmd.setExecutor(executor)
    }

    open fun registerEvents(vararg events: Listener) {
        for (event in events) {
            server.pluginManager.registerEvents(event, this)
        }
    }

    open fun <T> getRegisteredPlugin(clazz: Class<T>) : T? {
        val rsp = server.servicesManager.getRegistration(clazz)
        if (rsp == null) {
            logger.severe("Service provider for ${clazz.name} not registered, disabling plugin.")
            server.pluginManager.disablePlugin(this)
            return null
        }
        return rsp.provider
    }

    @Suppress("UnstableApiUsage")
    open fun copyResource(resourceName: String): Boolean {
        val destination = File(dataFolder, resourceName)
        val resource = javaClass.classLoader.getResource(resourceName)
        if (resource == null) {
            logger.severe("The resource $resourceName does not exist.")
            return false
        }
        if (destination.exists()) {
            logger.info("The file $resourceName already exists.")
            return true
        }
        try {
            destination.createNewFile()
            Resources.asByteSource(resource).copyTo(Files.asByteSink(destination))
            logger.info("The file $resourceName has been created.")
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

}