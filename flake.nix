{
  description = "Java project development flake";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";

  outputs = {
    self,
    nixpkgs,
  }: let
    system = "x86_64-linux"; # Adjust if needed
    pkgs = import nixpkgs {inherit system;};
  in {
    devShell.${system} = pkgs.mkShell {
      buildInputs = [
        pkgs.openjdk21
        pkgs.maven
        pkgs.gradle
        pkgs.google-java-format.out
        pkgs.androidsdk
      ];
      shellHook = ''
        export ANDROID_HOME=${pkgs.androidsdk}
        code .
        fish
      '';
    };
  };
}
