package io.github.shogowada.scalajs.reactjs

import io.github.shogowada.scalajs.reactjs.infra.Pickler
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

trait ReactClassSpec {

  class This(self: js.Dynamic) {
    def state: State = Pickler.toScala[State](self.state)

    def props: Props = Pickler.toScala[Props](self.props)

    def refs(key: String): HTMLElement = self.refs.selectDynamic(key).asInstanceOf[HTMLElement]

    def setState(state: State): Unit = self.setState(Pickler.toJs(state))
  }

  type Props
  type State

  def getInitialState(): State = ???

  def render(): ReactElement

  var self: This = _

  def asNative: js.Object = {
    val nativeGetInitialState = js.ThisFunction.fromFunction1((newSelf: js.Dynamic) => {
      self = new This(newSelf)
      Pickler.toJs(getInitialState())
    })
    val nativeRender = js.ThisFunction.fromFunction1((newSelf: js.Dynamic) => {
      self = new This(newSelf)
      render()
    })
    js.Dynamic.literal(
      "getInitialState" -> nativeGetInitialState,
      "render" -> nativeRender
    )
  }
}
