# guac-testing

FIXME: my new library.

## Usage

FIXME: write usage documentation!

Invoke a library API function from the command-line:

    $ clojure -X site.djei.guac-testing/foo :a 1 :b '"two"'
    {:a 1, :b "two"} "Hello, World!"

## Testing with podman

    podman run --name guacd -d -p 4822:4822 docker.io/guacamole/guacd
    podman run --name guac -v $PWD/guac_home/:/guac_home -e GUACAMOLE_HOME=/guac_home -e GUACD_HOSTNAME=192.168.0.16 -d -p 8085:8080/tcp docker.io/guacamole/guacamole:1.3.0
