{pkgs ? import <nixpkgs> {}}:
pkgs.mkShell {
  buildInputs = with pkgs; [
    openjdk21
    maven
    gradle
    google-java-format.out
    androidsdk
  ];

  shellHook = ''
    export ANDROID_HOME=${pkgs.androidsdk}
    code .
    fish
  '';
}
