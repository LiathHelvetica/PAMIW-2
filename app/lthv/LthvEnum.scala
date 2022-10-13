package lthv

class LthvEnum(val value: String) {
  override implicit def toString: String = value
}
