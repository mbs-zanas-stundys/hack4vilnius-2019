# 🧹 Hack4Vilnius - Susitvarkom

"Susitvarkom" is a web-based app that displays publicly available waste management
data of Vilnius' city in an intuitive & user-friendly way.  
Initial idea was conceived during a "Hack4Vilnius" hackathon on late September, 2019.  
We had only three days to implement it and managed to produce the groundwork you see here.

**The main goal of this application is to:**

- Display public data in a user-friendly manner that give insight into how Vilnius' waste is being handled
- Improve data transparency between Vilnius' residents and providers of waste management services
- Increase awareness of unmaintained waste bins / missed scheduled unloads
- Hopefully, make Vilnius a cleaner city to live

# ✌ Our team

Our hackathon team members:

- 😎 Team Lead • Mikas Stankevičius
- 🎨 Design • Lina Miliauskaitė
- ✨ Frontend • Žanas Stundys
- ⚡ Backend • Donatas Zubavičius

We're all from a lovely place called [Metasite](https://www.metasite.net/).

# 🤝 Contribution

This is an open-source project that's open for collaboration. So, if you'd like to contribute. Feel free to open a pull request. It might get added to the project ;)

# 💻 How to start developing locally

### 1. Prerequisites

- Backend
  - Docker
  - Java JDK 11
- Frontend
  - Node 12

### 2. Backend

```
cd api
docker-compose up
./gradlew clean bootRun
```

### 3. Frontend

```
cd ui
npm install
npm start
```
