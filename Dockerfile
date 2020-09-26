FROM amazoncorretto:11

COPY . /build
WORKDIR /build
RUN apt-get update && apt-get upgrade && apt-get install -y \
    pandoc  \
  && rm -rf /var/lib/apt/lists/*
RUN cd updater/ && ./build-docker.sh

FROM amazoncorretto:11

EXPOSE 27767

RUN mkdir /app

COPY --from=build /build/updater/tmp/ForestDefense-app/* /app/
COPY --from=build /build/updater/web/* /web2/
COPY --from=build /build/updater/run-docker.sh /app


ENTRYPOINT ["/app/bin/ForestDefense"]
