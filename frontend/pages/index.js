import React, { useState } from 'react'
import s from 'styled-components'
import axios from 'axios'

const Wrapper = s.div`
  padding: 2rem 5rem;
`

const NavBar = s.div`
  margin-bottom: 3rem;
`

const Logo = s.div`
  float: left;
  margin-right: 5rem;
  font-size: 30px;
`

const ResultDiv = s.div`
  margin-bottom: 3rem;
  max-width: 50%
`

const URLText = s.p`
  font-size: 80%;
`

const Home = () => {
  const [results, setResults] = useState(null)
  const [loading, setLoading] = useState(false)
  const [query, setQuery] = useState('')

  const handleSearchOnClick = (query = 'upenn') => {
    setLoading(true)
    axios.get(`http://localhost:8080/query?query=${query}`).then(resp => {
      const { data: { query } } = resp
      setLoading(false)
      setResults(query)
    })
  }

  return (
    <Wrapper>
      <NavBar>
        <Logo> PennSearch </Logo>
        <div className="field has-addons">
          <div className="control">
            <input
              className="input"
              type="text"
              placeholder="Search for anything @upenn"
              size="50"
              onChange={e => setQuery(e.target.value)}
              onKeyPress={e => {
                if (e.key === 'Enter') handleSearchOnClick(query)
              }}
            />
          </div>
          <div className="control" onClick={() => handleSearchOnClick(query)}>
            <a className="button is-info">
              Search
            </a>
          </div>
        </div>
      </NavBar>

      <div>
        {loading && <h4> Loading... </h4>}
        {!loading && results && results.length === 0 && <h4> Sorry, there are no corresponding results for your query. </h4>}
        {!loading && results && results.map(({ title, url, content }) => {
          return (
            <ResultDiv>
              <a href={url} target="_blank">
                <URLText> {url} </URLText>
                <h2> {title} </h2>
              </a>
              <p> {content} </p>
            </ResultDiv>
          )
        })}
      </div>
    </Wrapper>
  )
}

export default Home