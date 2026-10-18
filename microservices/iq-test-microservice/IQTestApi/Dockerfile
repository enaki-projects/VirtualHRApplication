# syntax=docker/dockerfile:1
# iq-test: ASP.NET Core 8 + ML.NET 4, plain HTTP on 2014. Multi-arch (arm64/amd64).
# The publish output is framework-dependent IL, so the SDK stage runs on the build
# host's own platform and the result runs on any target architecture.
FROM --platform=$BUILDPLATFORM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src
COPY IQTestApi/IQTestApi.csproj IQTestApi/
COPY IQTestServices/IQTestServices.csproj IQTestServices/
RUN dotnet restore IQTestApi/IQTestApi.csproj
COPY . .
RUN dotnet publish IQTestApi/IQTestApi.csproj -c Release -o /app/publish --no-restore /p:UseAppHost=false

FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final
WORKDIR /app
# The service reads AdvancedRaven/* relative to the working directory and the
# model zip next to the assembly; both are in the publish output.
COPY --from=build /app/publish .
ENV ASPNETCORE_HTTP_PORTS=2014 \
    DOTNET_gcServer=0
EXPOSE 2014
# Built-in non-root user of the aspnet:8.0 image.
USER $APP_UID
ENTRYPOINT ["dotnet", "IQTestApi.dll"]
