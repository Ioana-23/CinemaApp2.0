import { useState } from 'react'
import {CardText} from "react-bootstrap";

interface ReadMoreProps {
    text: string
    amountOfWords?: number
}

export const ReadMore = ({text, amountOfWords = 15 }: ReadMoreProps) => {
    const [isExpanded, setIsExpanded] = useState(false)
    const splittedText = text.split(' ')
    const itCanOverflow = splittedText.length > amountOfWords
    const beginText = itCanOverflow
        ? splittedText.slice(0, amountOfWords - 1).join(' ')
        : text
    const endText = splittedText.slice(amountOfWords - 1).join(' ')

    const handleKeyboard = (e) => {
        if (e.code === 'Space' || e.code === 'Enter') {
            setIsExpanded(!isExpanded)
        }
    }

    return (
        <div style={{color: '#767676'}}>
            {beginText}
            {itCanOverflow && (
                <>
                    {!isExpanded && <span>... </span>}
                    {isExpanded && (
                        <>
                            <span
                                className={`${!isExpanded && 'hidden'}`}
                                aria-hidden={!isExpanded}
                            >
                        { } {endText}
                                          </span>
                        </>)}
                    <span
                        className='text-violet-400 ml-2'
                        role="button"
                        tabIndex={0}
                        aria-expanded={isExpanded}
                        onKeyDown={handleKeyboard}
                        onClick={() => setIsExpanded(!isExpanded)}
                    >
            {isExpanded ? <CardText style={{color: '#2B2E4A'}}>show less</CardText> : <CardText style={{color: '#2B2E4A'}}>show more</CardText>}
          </span>
                </>
            )}
        </div>
    )
}