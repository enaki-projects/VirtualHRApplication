FROM mcr.microsoft.com/dotnet/aspnet:5.0 AS base
WORKDIR /app
EXPOSE 2014
EXPOSE 5000
EXPOSE 5001

ENV PORT=2014

FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /src
COPY ["IQTestApi/IQTestApi.csproj", "IQTestApi/"]
COPY ["IQTestServices/IQTestServices.csproj", "IQTestServices/"]
RUN dotnet restore "IQTestApi/IQTestApi.csproj"

COPY . .
WORKDIR "/src/IQTestApi"
RUN dotnet build "IQTestApi.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "IQTestApi.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
# ENTRYPOINT ["dotnet", "IQTestApi.dll"]
CMD ASPNETCORE_URLS=http://*:$PORT "dotnet" "IQTestApi.dll"
