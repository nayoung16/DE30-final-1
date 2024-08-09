import { useEffect, useRef } from "react";
import Hero from "./examples/Hero.js";

function Index({ isLogin, setIsLogin }) {
  const mainRef = useRef(null);

  useEffect(() => {
    document.documentElement.scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
    if (mainRef.current) {
      mainRef.current.scrollTop = 0;
    }
  }, []);

  return (
    <main ref={mainRef}>
      <Hero isLogin={isLogin} setIsLogin={setIsLogin}/>
    </main>
  );
}

export default Index;
