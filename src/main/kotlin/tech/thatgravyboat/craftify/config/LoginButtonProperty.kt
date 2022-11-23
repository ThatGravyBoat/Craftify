package tech.thatgravyboat.craftify.config

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.USound
import gg.essential.vigilance.data.PropertyInfo
import gg.essential.vigilance.gui.ExpandingClickEffect
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.gui.settings.SettingComponent
import gg.essential.vigilance.utils.onLeftClick
import tech.thatgravyboat.craftify.Initializer
import tech.thatgravyboat.craftify.server.LoginServer

class LoginButtonProperty: PropertyInfo() {

    override fun createSettingComponent(initialValue: Any?): SettingComponent {
        return LoginComponent("Login", {
            if (Config.modMode == 1) {
                LoginServer.createServer()
            } else if (Config.modMode != 1) {
                Initializer.getAPI()?.restart()
            }
        },
        {
            Config.modMode != 0
        })
    }
}

class LoginComponent(placeholder: String, private val callback: () -> Unit, private val canClick: () -> Boolean) : SettingComponent() {

    private var textState: State<String> = BasicState(placeholder.ifEmpty { "Activate" })

    private val container by UIBlock(VigilancePalette.getButton()).constrain {
        width = ChildBasedSizeConstraint() + 14.pixels
        height = ChildBasedSizeConstraint() + 8.pixels
        color = SuppliedColoredConstraint {
            return@SuppliedColoredConstraint if (canClick.invoke()) VigilancePalette.getButton() else VigilancePalette.getDisabled()
        }
    } childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        UIWrappedText(
            textState.get(),
            trimText = true,
            shadowColor = VigilancePalette.getTextShadow()
        ).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = width.coerceAtMost(300.pixels)
            height = 10.pixels
            color = SuppliedColoredConstraint {
                return@SuppliedColoredConstraint if (canClick.invoke()) VigilancePalette.getText() else VigilancePalette.getTextDisabled()
            }
        } childOf container

        enableEffect(ExpandingClickEffect(VigilancePalette.getPrimary().withAlpha(0.5f), scissorBoundingBox = container))

        container.onMouseEnter {
            if (canClick.invoke()) {
                container.animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getButtonHighlight().toConstraint())
                }
            }
        }.onMouseLeave {
            if (canClick.invoke()) {
                container.animate {
                    setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getButton().toConstraint())
                }
            }
        }.onLeftClick {
            if (canClick.invoke()) {
                USound.playButtonPress()
                callback()
            }
        }
    }
}