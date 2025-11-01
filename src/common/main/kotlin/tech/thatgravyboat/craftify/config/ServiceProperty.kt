@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package tech.thatgravyboat.craftify.config

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.state.State
import gg.essential.elementa.utils.withAlpha
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.PropertyInfo
import gg.essential.vigilance.gui.SettingsCategory
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.gui.settings.DropDownComponent
import gg.essential.vigilance.gui.settings.SettingComponent
import gg.essential.vigilance.utils.onLeftClick
import tech.thatgravyboat.craftify.services.ServiceTypes
import tech.thatgravyboat.craftify.services.config.ServiceConfig
import java.awt.Color

class ServiceProperty : PropertyInfo() {
    override fun createSettingComponent(initialValue: Any?): SettingComponent {
        return ServiceComponent(initialValue as? String ?: "disabled")
    }
}

private val SERVICES by lazy {
    val services = linkedMapOf("disabled" to "Disabled")

    for (type in ServiceTypes.SERVICES) {
        if (!type.isAvailable) continue
        services[type.id] = type.name
    }

    return@lazy services
}

private fun getSelectedService(index: Int): String? = runCatching {
    SERVICES.keys.elementAt(index)
}.getOrNull()

class ServiceComponent(value: String) : SettingComponent() {

    private val dropdown by DropDownComponent(SERVICES.keys.indexOf(value).coerceAtLeast(0), SERVICES.values.toList()).constrain {
        x = (-17).pixels()
    } childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = 17.pixels()
        }

        val overlay = UIBlock(Color.BLACK.withAlpha(0.5f)).constrain {
            x = 0.pixels()
            y = 0.pixels()
            width = RelativeWindowConstraint()
            height = RelativeWindowConstraint()
        }

        val settingsBackground = UIBlock(VigilancePalette.getMainBackground()).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 50.percent()
            height = 80.percent()
        }
        .effect(ScissorEffect())
        .effect(OutlineEffect(VigilancePalette.getDarkDivider(), 3f))
        .onLeftClick {
            it.stopPropagation()
        }
        .childOf(overlay)

        var category = SettingsCategory(Category(
            name = "",
            items = ServiceTypes.fromId(getSelectedService(dropdown.selectedIndex.get()))?.config ?: emptyList(),
            description = null
        )).constrain {
            width = 100.percent()
            height = 100.percent()
        } childOf settingsBackground

        overlay.onLeftClick {
            runWindowOnRender {
                it.removeChild(overlay)
                it.removeFloatingComponent(overlay)

                ServiceConfig.save()
            }
        }

        val colorState = object : State<Color>() {
            override fun get(): Color = if (isDisabled()) {
                VigilancePalette.getDisabled()
            } else {
                VigilancePalette.getButton()
            }
        }
        // constraint must be manually constructed due to the internals just calling get on initial creation
        val button = UIBlock(ConstantColorConstraint(colorState)).constrain {
            x = RelativeConstraint(1f) - 17.pixels()
            width = 17.pixels()
            height = 17.pixels()
        }.onMouseEnter {
            if (!isDisabled()) {
                this.setColor(VigilancePalette.getButtonHighlight())
            }
        }.onMouseLeave {
            this.setColor(if (isDisabled()) VigilancePalette.getDisabled() else VigilancePalette.getButton())
        }.onLeftClick {
            if (!isDisabled()) {
                runWindowOnRender {
                    it.addChild(overlay)
                    it.addFloatingComponent(overlay)
                }
            }
        } childOf this

        UIImage.ofResourceCached("/craftify/gear.png").constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 9.pixels()
            height = 9.pixels()
        } childOf button

        dropdown.selectedIndex.onSetValue { newValue ->
            getSelectedService(newValue)?.let {
                changeValue(it)
                category.hide(true)
                category = SettingsCategory(Category(
                    name = "",
                    items = ServiceTypes.fromId(it)?.config ?: emptyList(),
                    description = null
                )).constrain {
                    width = 100.percent()
                    height = 100.percent()
                } childOf settingsBackground
            }
        }
    }

    override fun closePopups(instantly: Boolean) {
        dropdown.collapse(instantly)
    }

    private fun isDisabled(): Boolean = getSelectedService(dropdown.selectedIndex.get()) == "disabled"

    private fun runWindowOnRender(action: (Window) -> Unit) {
        Window.enqueueRenderOperation {
            Window.ofOrNull(this@ServiceComponent)?.let {
                action(it)
            }
        }
    }
}